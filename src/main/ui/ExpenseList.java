package ui;

import model.Expense;
import model.ExpenseTracker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ExpenseList implements ListSelectionListener {
    private ExpenseTracker expenseTracker;
    private JButton deleteButton;
    private JList list;
    private DefaultListModel listModel;

    public ExpenseList(ExpenseTracker expenseTracker, JButton deleteButton) {
        this.expenseTracker = expenseTracker;
        this.deleteButton = deleteButton;
        this.listModel = new DefaultListModel();
        this.list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);

        updateExpenses();
    }

    public void updateExpenses() {
        listModel.removeAllElements();

        for (Expense e : expenseTracker.getAllExpenses()) {
            listModel.addElement(e.getSummary());
        }

        list.setSelectedIndex(0);
        deleteButton.setEnabled(true);
    }

    public JList getList() {
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

//    public class CustomListCellRenderer extends JLabel implements ListCellRenderer<Object> {
//        @Override
//        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
//                                                      boolean isSelected, boolean cellHasFocus) {
//            if (value instanceof Expense) {
//                Expense item = (Expense) value;
//                setText(item.getSummary());
//                setIcon(item.getIcon());
//                setOpaque(true);
//
//                // Customize appearance based on selection
//                if (isSelected) {
//                    setBackground(list.getSelectionBackground());
//                    setForeground(list.getSelectionForeground());
//                } else {
//                    setBackground(list.getBackground());
//                    setForeground(list.getForeground());
//                }
//            }
//
//            return this;
//        }
//    }
}
