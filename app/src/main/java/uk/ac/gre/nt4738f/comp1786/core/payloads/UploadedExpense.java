package uk.ac.gre.nt4738f.comp1786.core.payloads;

import uk.ac.gre.nt4738f.comp1786.core.entities.Expense;

public class UploadedExpense extends Expense {
    public String name;

    public UploadedExpense(String tripName, Expense expense) {
        super(expense.id, expense.tripId, expense.type, expense.time, expense.amount);
        name = tripName;
    }
}