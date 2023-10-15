package ui;

import model.Category;
import model.Expense;

import java.time.LocalDate;
import java.util.List;

// Initiates example expenses and categories (for testing)
public class CreateExamples {
    List<Expense> allExpenses;
    List<Category> allCategories;
    LocalDate today;

    // EFFECTS: Takes in allExpenses and allCategories as parameters and sets them as fields
    public CreateExamples (List<Expense> allExpenses, List<Category> allCategories) {
        this.allExpenses = allExpenses;
        this.allCategories = allCategories;
        this.today = LocalDate.now();
        create();
    }

    // EFFECTS: creates example expenses and categories containing them
    public void create() {
        Expense E1 = new Expense();
        Expense E2 = new Expense();
        Expense E3 = new Expense();
        Expense E4 = new Expense();
        Expense E5 = new Expense();
        allExpenses.add(E1);
        allExpenses.add(E2);
        allExpenses.add(E3);
        allExpenses.add(E4);
        allExpenses.add(E5);

        Category C1 = new Category("grocery");
        Category C2 = new Category("clothing");
        Category C3 = new Category("rent");
        allCategories.add(C1);
        allCategories.add(C2);
        allCategories.add(C3);

        E1.setAmount(100);
        E1.setPlace("no frills");
        E1.setCategory("grocery");
        C1.add(E1);

        E2.setAmount(20);
        E2.setDate(today.minusMonths(1).toString());
        E2.setPlace("save on food");
        E2.setCategory("grocery");
        C1.add(E2);

        E3.setAmount(55);
        E3.setDate(today.minusDays(7).toString());
        E3.setPlace("ubc book store");
        E3.setCategory("clothing");
        C2.add(E3);

        E4.setAmount(210);
        E4.setDate(today.minusDays(3).toString());
        E4.setPlace("lululemon");
        E4.setCategory("clothing");
        C2.add(E4);

        E5.setAmount(1000);
        E5.setDate(today.minusDays(6).toString());
        E5.setPlace("ubc housing");
        E5.setCategory("rent");
        C3.add(E5);
    }
}
