package ui;

import model.Category;
import model.ExpenseTracker;

import java.util.List;
import javax.swing.*;
import java.awt.*;

// Represents a pie chart statistics of the categorical expenses
public class PieChartPanel extends JPanel {
    private static final int DIAMETER = 250;
    private static final int LABEL_DISTANCE_FROM_CENTER = 70;
    ExpenseTracker expenseTracker;

    // EFFECTS: constructs a pie chart panel for the given expense tracker
    public PieChartPanel(ExpenseTracker expenseTracker) {
        super();
        this.expenseTracker = expenseTracker;
    }

    // MODIFIES: this
    // EFFECTS: draws the pie chart with labels
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        List<Category> allCategories = expenseTracker.getAllCategories();
        double total = expenseTracker.calculateTotalAmount();
        int startAngle = 0;
        int x = (this.getX() + this.getWidth()) / 2 - DIAMETER / 2;
        int y = (this.getY() + this.getHeight()) / 2 - DIAMETER / 2;

        drawPieChart(g, allCategories, total, startAngle, x, y);
        drawLabels(g, allCategories, total, startAngle, x, y);
    }

    // MODIFIES: this
    // EFFECTS: draws the pie chart
    private void drawPieChart(Graphics g, List<Category> allCategories, double total, int startAngle, int x, int y) {
        for (Category c : allCategories) {
            double arcAngle = (int) (c.totalAmount() / total * 360 + 1);

            g.setColor(c.getIconColor());
            g.fillArc(x, y, DIAMETER, DIAMETER, startAngle, (int) arcAngle);

            startAngle += (int) arcAngle;
        }
    }

    // MODIFIES: this
    // EFFECTS: draws the labels
    private void drawLabels(Graphics g, List<Category> allCategories, double total, int startAngle, int x, int y) {
        for (Category c : allCategories) {
            if (!(c.equals(expenseTracker.getCoNC()) && c.getExpenses().isEmpty())) {
                double arcAngle = (int) (c.totalAmount() / total * 360 + 1);
                double midPointAngle = Math.toRadians(startAngle + arcAngle / 2);
                String label = c.getLabel() + " ($" + (int) c.totalAmount() + ")";
                int labelX = (int) (x + DIAMETER / 2 + (LABEL_DISTANCE_FROM_CENTER * Math.cos(midPointAngle))
                        - 3 * label.length());
                int labelY = (int) (y + DIAMETER / 2 - (LABEL_DISTANCE_FROM_CENTER * Math.sin(midPointAngle)));

                Font font = new Font("Arial", Font.BOLD, 15);
                g.setFont(font);
                g.setColor(Color.BLACK);
                g.drawString(label, labelX, labelY);

                startAngle += (int) arcAngle;
            }
        }
    }
}