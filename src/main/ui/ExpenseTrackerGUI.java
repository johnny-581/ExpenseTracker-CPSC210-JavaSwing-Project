package ui;

import model.Expense;
import model.ExpenseTracker;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExpenseTrackerGUI extends JPanel implements ActionListener {
    private static final String JSON_STORE = "./data/expenseTracker.json";
    private static final String LOAD_DATA_MESSAGE = "Do you want to load expenses and categories from file?";
    private static final String SAVE_CHANGES_MESSAGE = "Do you want to save your changes?";
    private static final String RECORD_STRING = "Record New Expense";
    private static final String DELETE_STRING = "Delete Selected";
    private static final String NEW_EXPENSE_DIALOG_TITLE = "New Expense";
    private static final String EDIT_EXPENSE_DIALOG_TITLE = "Edit Expense";

    private ExpenseTracker expenseTracker;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    private JFrame frame;
    private ExpenseList expenseList;
    private JButton recordButton;
    private JButton deleteButton;

    // EFFECTS: constructs an expense tracker GUI
    public ExpenseTrackerGUI() {
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
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // EFFECTS: adds components to the content pane
    private void buildContentPane() {
        setLayout(new BorderLayout());
//        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(recordButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createVerticalGlue());
        add(buttonPanel, BorderLayout.CENTER);

        ImageIcon sampleImage = new ImageIcon(System.getProperty("user.dir") + "/images/SamplePieChart.png");
        JLabel imageLabel = new JLabel(sampleImage);
        add(imageLabel, BorderLayout.EAST);

        expenseList = (new ExpenseList(expenseTracker, deleteButton));
        addMouseListenerToList();
        add(new JScrollPane(expenseList.getList()), BorderLayout.SOUTH);

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
                    new ExpenseInfoDialog(expenseList, expenseTracker, selectedExpense, EDIT_EXPENSE_DIALOG_TITLE);
                }
            }
        });
    }

    // MODIFIES: this, expenseList, expenseTracker
    // EFFECTS: Handles button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(RECORD_STRING)) {
            Expense expense = new Expense(expenseTracker.getCONC(), 0);
            new ExpenseInfoDialog(expenseList, expenseTracker, expense, NEW_EXPENSE_DIALOG_TITLE);
        }
        if (e.getActionCommand().equals(DELETE_STRING)) {
            expenseList.deleteExpense();
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
