package ui;

import model.EventLog;
import model.Expense;
import model.ExpenseTracker;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents the main window of the expense tracker GUI
public class MainGUI extends JPanel implements ActionListener {
    private static final String JSON_STORE = "./data/expenseTracker.json";
    private static final String LOAD_DATA_MESSAGE = "Do you want to load expenses and categories from file?";
    private static final String SAVE_CHANGES_MESSAGE = "Do you want to save your changes?";
    private static final String RECORD_STRING = "Record New Expense";
    private static final String DELETE_STRING = "Delete Selected";
    public static final String NEW_EXPENSE_DIALOG_TITLE = "New Expense";
    public static final String EDIT_EXPENSE_DIALOG_TITLE = "Edit Expense";

    private ExpenseTracker expenseTracker;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    private final JFrame frame;
    private ExpenseList expenseList;
    private final JButton recordButton;
    private final JButton deleteButton;
    private PieChartPanel pieChartPanel;

    // EFFECTS: constructs an expense tracker GUI
    public MainGUI() {
        expenseTracker = new ExpenseTracker();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        recordButton = new JButton(RECORD_STRING);
        deleteButton = new JButton(DELETE_STRING);

        recordButton.setActionCommand(RECORD_STRING);
        deleteButton.setActionCommand(DELETE_STRING);
        recordButton.addActionListener(this);
        deleteButton.addActionListener(this);

        frame = new JFrame("Your Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(createWindowListener());
        frame.setContentPane(this);
        frame.setSize(450, 650);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: adds components to the content pane
    private void buildContentPane() {
        setLayout(new BorderLayout());

        pieChartPanel = new PieChartPanel(expenseTracker);
        add(pieChartPanel, BorderLayout.CENTER);

        JPanel listAndButtonPanel = new JPanel();
        listAndButtonPanel.setLayout(new BoxLayout(listAndButtonPanel, BoxLayout.Y_AXIS));
        listAndButtonPanel.add(Box.createVerticalGlue());
        listAndButtonPanel.add(Box.createVerticalStrut(10));
        listAndButtonPanel.add(recordButton);
        listAndButtonPanel.add(deleteButton);
        recordButton.setAlignmentX(CENTER_ALIGNMENT);
        deleteButton.setAlignmentX(CENTER_ALIGNMENT);
        listAndButtonPanel.add(Box.createVerticalStrut(10));
        expenseList = new ExpenseList(expenseTracker, deleteButton);
        addMouseListenerToList();
        listAndButtonPanel.add(new JScrollPane(expenseList.getList()));
        listAndButtonPanel.add(Box.createVerticalGlue());
        add(listAndButtonPanel, BorderLayout.SOUTH);

        setOpaque(true);
        revalidate();
        repaint();
    }

    // MODIFIES: this, expenseList, expenseTracker
    // EFFECTS: adds mouse listener to the expense list that listens for double click
    private void addMouseListenerToList() {
        expenseList.getList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = expenseList.getList().locationToIndex(e.getPoint());
                    Expense selectedExpense = expenseTracker.getAllExpenses().get(index);
                    new ExpenseInfoDialog(expenseList, pieChartPanel, expenseTracker,
                            selectedExpense, EDIT_EXPENSE_DIALOG_TITLE);
                }
            }
        });
    }

    // MODIFIES: this, expenseList, expenseTracker
    // EFFECTS: Handles button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(RECORD_STRING)) {
            Expense expense = new Expense(expenseTracker.getCoNC(), 0);
            new ExpenseInfoDialog(expenseList, pieChartPanel, expenseTracker, expense, NEW_EXPENSE_DIALOG_TITLE);
        }
        if (e.getActionCommand().equals(DELETE_STRING)) {
            expenseList.deleteExpense();
            pieChartPanel.repaint();
        }
    }

    // EFFECTS: reminds the user to load and save data on startup and closing
    private WindowListener createWindowListener() {
        return new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                askToLoadData();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                askToSaveChanges();

                EventLog el = EventLog.getInstance();
                for (Event event : el) {
                    System.out.println(event.toString() + "\n");
                }
            }
        };
    }

    // MODIFIES: this, expenseList, expenseTracker
    // EFFECTS: displays dialog asking whether to load data
    private void askToLoadData() {
        int choice = JOptionPane.showConfirmDialog(frame, LOAD_DATA_MESSAGE,
                "Load Data", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            loadExpenseTracker();
        }

        buildContentPane();
    }

    // MODIFIES: this
    // EFFECTS: displays dialog asking whether to save changes
    private void askToSaveChanges() {
        int choice = JOptionPane.showConfirmDialog(frame, SAVE_CHANGES_MESSAGE,
                "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            saveExpenseTracker();
            frame.dispose();
        } else if (choice == JOptionPane.NO_OPTION) {
            frame.dispose();
        }
    }

    // MODIFIES: this
    // EFFECTS: loads expense tracker from file
    private void loadExpenseTracker() {
        try {
            expenseTracker = jsonReader.read();
            System.out.println("Loaded your expenses and categories from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: saves the expense tracker to file
    private void saveExpenseTracker() {
        try {
            jsonWriter.open();
            jsonWriter.write(expenseTracker);
            jsonWriter.close();
            System.out.println("Saved your expenses and categories to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }
}
