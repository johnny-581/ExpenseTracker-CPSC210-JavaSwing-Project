package model;

import java.util.ArrayList;
import java.util.List;

public class Category extends CollectionOfEntries {
    private List<Place> places;

    public Category(String name) {
        this.name = name;
        this.places = new ArrayList<>();
    }

}
