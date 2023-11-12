package ui;

import model.ExpenseTracker;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class ExpenseTrackerGUI extends JPanel {
    private static final String JSON_STORE = "./data/expenseTracker.json";
    private static final String LOAD_DATA_MESSAGE = "Do you want to load expenses and categories from file?";
    private static final String SAVE_CHANGES_MESSAGE = "Do you want to save your changes?";

    private ExpenseTracker expenseTracker;
    private final Scanner input;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    private JFrame frame;

    public ExpenseTrackerGUI() {
        expenseTracker = new ExpenseTracker();
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        createAndShowGUI();
    }

    // Method adapted from Java Swing ListDemoProject
    // EFFECTS: creates and displays the main frame
    private void createAndShowGUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(true); //content panes must be opaque

        frame = new JFrame("Your Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setContentPane(this);
        frame.addWindowListener(createWindowListener());

        add((new ExpenseList(expenseTracker).getList()));

        frame.pack();
        frame.setVisible(true);
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

    // EFFECTS: displays dialog asking whether to load data
    private void askToLoadData() {
        int choice = JOptionPane.showConfirmDialog(frame, LOAD_DATA_MESSAGE,
                "Load Data", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            loadExpenseTracker();
        }
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
