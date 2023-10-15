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

    Expense expense1;
    Expense expense2;
    Expense expense3;
    Expense expense4;
    Expense expense5;
    Category category1;
    Category category2;
    Category category3;

    // EFFECTS: Takes in allExpenses and allCategories as parameters and sets them as fields
    public CreateExamples(List<Expense> allExpenses, List<Category> allCategories) {
        this.allExpenses = allExpenses;
        this.allCategories = allCategories;
        this.today = LocalDate.now();
        create();
    }

    // EFFECTS: creates example expenses and categories containing them
    public void create() {
        expense1 = new Expense();
        expense2 = new Expense();
        expense3 = new Expense();
        expense4 = new Expense();
        expense5 = new Expense();
        allExpenses.add(expense1);
        allExpenses.add(expense2);
        allExpenses.add(expense3);
        allExpenses.add(expense4);
        allExpenses.add(expense5);

        category1 = new Category("grocery");
        category2 = new Category("clothing");
        category3 = new Category("rent");
        allCategories.add(category1);
        allCategories.add(category2);
        allCategories.add(category3);

        setExpenses();
    }

    // EFFECTS: sets example expenses
    private void setExpenses() {
        expense1.setAmount(100);
        expense1.setPlace("no frills");
        expense1.setCategory("grocery");

        expense2.setAmount(20);
        expense2.setDate(today.minusMonths(1).toString());
        expense2.setPlace("save on food");
        expense2.setCategory("grocery");

        expense3.setAmount(55);
        expense3.setDate(today.minusDays(7).toString());
        expense3.setPlace("ubc book store");
        expense3.setCategory("clothing");

        expense4.setAmount(210);
        expense4.setDate(today.minusDays(3).toString());
        expense4.setPlace("lululemon");
        expense4.setCategory("clothing");

        expense5.setAmount(1000);
        expense5.setDate(today.minusDays(6).toString());
        expense5.setPlace("ubc housing");
        expense5.setCategory("rent");

        addExpensesToCategories();
    }

    // EFFECTS: adds example expenses to categories
    private void addExpensesToCategories() {
        category1.add(expense1);
        category1.add(expense2);
        category2.add(expense3);
        category2.add(expense4);
        category3.add(expense5);
    }
}
