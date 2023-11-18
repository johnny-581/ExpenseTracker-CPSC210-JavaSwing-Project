package ui;

import exceptions.InvalidDateException;
import exceptions.InvalidInputException;
import model.Category;
import model.Expense;
import model.ExpenseTracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static model.Expense.NAME_OF_NO_PLACE;

// Represents an expense info dialog (pop-up window)
public class ExpenseInfoDialog implements ActionListener, DocumentListener {
    private static final String FINISH_STRING = "Finish";

    private final ExpenseList expenseList;
    private final PieChartPanel pieChartPanel;
    private final ExpenseTracker expenseTracker;
    private final Expense expense;
    private final JDialog dialog;
    private final JPanel contentPanel;
    private final JButton finishButton;
    private final JLabel statusLabel;

    private JTextField amountField;
    private JTextField dateField;
    private JTextField placeField;
    private JComboBox<String> categoryComboBox;

    // EFFECTS: constructs and displays an expense info dialog, where the user can edit the
    //          amount, date, place, and category of an expense.
    public ExpenseInfoDialog(ExpenseList expenseList, PieChartPanel pieChartPanel,
                             ExpenseTracker expenseTracker, Expense expense, String title) {
        this.expenseList = expenseList;
        this.pieChartPanel = pieChartPanel;
        this.expenseTracker = expenseTracker;
        this.expense = expense;
        this.dialog = new JDialog();
        this.contentPanel = new JPanel(new BorderLayout());
        this.finishButton = new JButton(FINISH_STRING);
        finishButton.setActionCommand(FINISH_STRING);
        finishButton.addActionListener(this);
        this.statusLabel = new JLabel("");

        buildContentPane();
        initiateDialog(title);
    }

    // MODIFIES: this
    // EFFECTS: initiates the dialog frame
    private void initiateDialog(String title) {
        dialog.setTitle(title);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        dialog.setContentPane(contentPanel);
        dialog.setResizable(false);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: adds components to the content panel
    private void buildContentPane() {
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        contentPanel.add(statusLabel, BorderLayout.NORTH);
        addInputPanel();

        // adds buttonPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(finishButton);
        buttonPanel.add(Box.createHorizontalGlue());
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: adds input panel with labels and text fields to the contentPane
    private void addInputPanel() {
        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField();
        JLabel dateLabel = new JLabel("Date(YYYY-MM-DD):");
        dateField = new JTextField();
        JLabel placeLabel = new JLabel("Place:");
        placeField = new JTextField();
        JLabel categoryLabel = new JLabel("Category:");
        createCategoryComboBox();
        addExpenseInfoToFields();

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));
        inputPanel.add(amountLabel);
        inputPanel.add(amountField);
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);
        inputPanel.add(placeLabel);
        inputPanel.add(placeField);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryComboBox);
        contentPanel.add(inputPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: sets up the categoryComboBox
    private void createCategoryComboBox() {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();

        for (Category c : expenseTracker.getAllCategories()) {
            comboBoxModel.addElement(c.getLabel());
        }

        categoryComboBox = new JComboBox<>(comboBoxModel);
        categoryComboBox.setEditable(true);
    }

    // MODIFIES: this
    // EFFECTS: adds expense into to the corresponding fields
    private void addExpenseInfoToFields() {
        double amount = expense.getAmount();
        String date = expense.getDate().toString().substring(0, 10);
        String place = expense.getPlace();
        String categoryLabel = expense.getCategory().getLabel();

        if (!(amount == 0)) {
            amountField.setText(Double.toString(amount));
        }

        dateField.setText(date);

        if (!place.equals(NAME_OF_NO_PLACE)) {
            placeField.setText(place);
        }

        categoryComboBox.getModel().setSelectedItem(categoryLabel);
    }

    // MODIFIES: this, expenseTracker, expenseList
    // EFFECTS: handles action event from the finish button
    public void actionPerformed(ActionEvent e) {
        String amount = amountField.getText();
        String date = dateField.getText();
        String place = placeField.getText();
        String categoryLabel = categoryComboBox.getSelectedItem().toString();
        Category category = expenseTracker.getCategoryFromLabel(categoryLabel);

        boolean noException = setExpenseAccordingToInput(amount, date, place, categoryLabel, category);
        boolean isNewExpense = !expenseTracker.getAllExpenses().contains(expense);

        if (isNewExpense && noException) {
            expenseTracker.addExpense(expense);
        }

        if (noException) {
            int index = expenseList.getList().getSelectedIndex();
            expenseList.updateExpenses();
            expenseList.getList().setSelectedIndex(index);
            pieChartPanel.repaint();
            dialog.dispose();
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the expense information according to user input
    private boolean setExpenseAccordingToInput(String amount, String date, String place,
                                            String categoryLabel, Category category) {
        try {
            expense.setAmount(amount);
            setDate(date);
            setPlace(place);
            setCategory(categoryLabel, category);
            return true;
        } catch (InvalidInputException ex) {
            statusLabel.setText("Input error: " + ex.getMessage());
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the place of the expense
    private void setPlace(String place) {
        if (place.equals("")) {
            expense.setNoPlace();
        } else {
            expense.setPlace(place);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the category of the expense
    private void setCategory(String categoryLabel, Category category) {
        boolean newExpense = !expenseTracker.getAllExpenses().contains(expense);
        boolean categoryExists = expenseTracker.categoryExists(categoryLabel);
        Category oldCategory = expense.getCategory();

        if (categoryExists) {
            expense.setCategory(category);
        } else if (newExpense) {
            category = new Category(categoryLabel);
            expenseTracker.addCategory(category);
            expense.setCategory(category);
        } else {
            expense.getCategory().setLabel(categoryLabel);
        }

        if (oldCategory.getExpenses().isEmpty() && !oldCategory.equals(expenseTracker.getCoNC())) {
            expenseTracker.deleteCategory(oldCategory);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the date of the expense
    private void setDate(String date) throws InvalidDateException {
        String oldDate = expense.getDate().toString().substring(0, 10);

        if (!date.matches("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")) {
            throw new InvalidDateException();
        }

        if (!oldDate.equals(date)) {
            expense.setDate(date);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {

    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}
