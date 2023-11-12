package ui;

import model.Expense;
import model.ExpenseTracker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ExpenseList implements ListSelectionListener {
    private ExpenseTracker expenseTracker;
    private JList list;
    private DefaultListModel listModel;
    private JScrollPane listScrollPane;

    public ExpenseList(ExpenseTracker expenseTracker) {
        this.expenseTracker = expenseTracker;
        this.listModel = new DefaultListModel();
        addExpenses();
        listModel.addElement("abc");
        listModel.addElement(expenseTracker.getAllExpenses().size());

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        listScrollPane = new JScrollPane(list);
    }

    private void addExpenses() {
        for (Expense e : expenseTracker.getAllExpenses()) {
            listModel.addElement(e.getSummary());
        }
    }

    public JScrollPane getList() {
        return listScrollPane;
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
