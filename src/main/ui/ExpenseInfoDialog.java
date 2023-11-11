package ui;

import model.Expense;
import model.ExpenseTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExpenseInfoDialog implements ActionListener {
    private ExpenseTracker expenseTracker;
    private JDialog dialog;
    private JPanel contentPane;

    public ExpenseInfoDialog(ExpenseTracker expenseTracker, Expense expense, String title) {
        this.expenseTracker = expenseTracker;
        this.dialog = new JDialog();
        this.contentPane = new JPanel(new GridLayout(5, 2));

        constructContentPane();
        dialog.setTitle(title);
//        dialog.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        dialog.setContentPane(contentPane);
        dialog.setSize(300, 200);
        dialog.setVisible(true);
    }

    private void constructContentPane() {
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();

        JLabel dateLabel = new JLabel("Date:");
        JTextField dateField = new JTextField();

        JLabel placeLabel = new JLabel("Place:");
        JTextField placeField = new JTextField();

        JLabel categoryLabel = new JLabel("Category:");
        JTextField categoryField = new JTextField();

        contentPane.add(amountLabel);
        contentPane.add(amountField);
        contentPane.add(dateLabel);
        contentPane.add(dateField);
        contentPane.add(placeLabel);
        contentPane.add(placeField);
        contentPane.add(categoryLabel);
        contentPane.add(categoryField);
    }

    public void actionPerformed(ActionEvent e) {

    }
}
