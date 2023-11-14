package ui;

import model.Expense;
import model.ExpenseTracker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

// Represents a JList of expenses reflecting the content of an expense tracker
public class ExpenseList implements ListSelectionListener {
    private static final int LIST_CELL_HEIGHT = 25;

    private final ExpenseTracker expenseTracker;
    private final JButton deleteButton;
    private final JList<Expense> list;
    private final DefaultListModel<Expense> listModel;

    // EFFECTS: constructs an expense list
    public ExpenseList(ExpenseTracker expenseTracker, JButton deleteButton) {
        this.expenseTracker = expenseTracker;
        this.deleteButton = deleteButton;
        this.listModel = new DefaultListModel<>();
        this.list = new JList<>(listModel);
        list.setCellRenderer(new CustomListCellRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(8);

        updateExpenses();
    }

    // MODIFIES: this
    // EFFECTS: update the expenses in the list according to the expense tracker
    public void updateExpenses() {
        listModel.removeAllElements();

        for (Expense e : expenseTracker.getAllExpenses()) {
            listModel.addElement(e);
        }

        list.setSelectedIndex(0);
        deleteButton.setEnabled(true);
    }

    // EFFECTS: returns the JList of expenses
    public JList<Expense> getList() {
        return list;
    }

    // Method adapted from java swing components ListDemoProject FireListener actionPreformed().
    // MODIFIES: this, expenseTracker
    // EFFECTS: removes the selected expense from the list module
    public void deleteExpense() {
        int index = list.getSelectedIndex();
        listModel.remove(index);
        expenseTracker.deleteExpense(index);

        int size = listModel.getSize();

        if (size == 0) {
            deleteButton.setEnabled(false);
        } else {
            if (index == listModel.getSize()) {
                index--;
            }

            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }

    // Represents a custom list cell renderer that renders the cells of the expense list
    public static class CustomListCellRenderer extends DefaultListCellRenderer {

        // MODIFIES: this
        // EFFECTS: sets the height of the cell
        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            size.height = LIST_CELL_HEIGHT;
            return size;
        }

        // EFFECTS: sets the text and icon in the cell according to the expense it represents
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            Expense expense = (Expense) value;
            setText(expense.getSummary());
            setIcon(expense.getIcon());
            setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
            setOpaque(true);

            return this;
        }
    }
}
