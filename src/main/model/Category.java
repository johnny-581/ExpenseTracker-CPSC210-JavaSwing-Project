package model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String label;
    private List<SpendingEntry> listOfEntries;

    public Category(String label) {
        this.label = label;
        listOfEntries = new ArrayList<>();
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public List<SpendingEntry> getEntries() {
        return listOfEntries;
    }

    public void add(SpendingEntry entry) {
        listOfEntries.add(entry);
    }

    public void remove(SpendingEntry entry) {
        listOfEntries.remove(entry);
    }

    public double getTotal() {
        double total = 0;

        for (SpendingEntry entry : listOfEntries) {
            total += entry.getAmount();
        }

        return total;
    }
}
