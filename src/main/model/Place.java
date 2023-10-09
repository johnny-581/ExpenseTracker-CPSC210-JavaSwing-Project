package model;

import java.util.ArrayList;
import java.util.List;

public class Place {
    private String name;
    private List<SpendingEntry> spendings;

    public void Place(String name) {
        this.name = name;
        spendings = new ArrayList<SpendingEntry>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addSpending(SpendingEntry s) {
        spendings.add(s);
    }

    public void removeSpending(SpendingEntry s) {
        spendings.remove(s);
    }
}
