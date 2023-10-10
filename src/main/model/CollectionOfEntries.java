package model;

import java.util.List;

public class CollectionOfEntries {
    protected String name;
    protected List<ExpenseEntry> entries;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<ExpenseEntry> getEntries() {
        return entries;
    }

    public void add(ExpenseEntry entry) {
        entries.add(entry);
    }

    public void remove(ExpenseEntry entry) {
        entries.remove(entry);
    }

    public double getTotal() {
        double total = 0;

        for (ExpenseEntry entry : entries) {
            total += entry.getAmount();
        }

        return total;
    }
}
