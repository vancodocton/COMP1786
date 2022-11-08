package uk.ac.gre.nt4738f.comp1786.core.entities.validators;


import java.util.ArrayList;

import uk.ac.gre.nt4738f.comp1786.core.entities.Expense;


public class ExpenseValidator implements IEntityValidator<Expense> {
    public EntityState Validate(Expense expense) {
        ArrayList<PropertyState> states = new ArrayList<>();
        if (expense.id < 0)
            states.add(PropertyState.Invalid(ErrorMessage.InvalidPropertyMessage("Id")));

        if (expense.tripId <= 0)
            states.add(PropertyState.Invalid(ErrorMessage.InvalidPropertyMessage("tripId")));

        if (expense.type == null || expense.type.trim().isEmpty())
            states.add(PropertyState.Invalid(ErrorMessage.NullOrEmptyPropertyMessage("type")));

        if (expense.time == null)
            states.add(PropertyState.Invalid(ErrorMessage.NullPropertyMessage("time")));

        if (expense.amount <= 0)
            states.add(PropertyState.Invalid(ErrorMessage.InvalidPositiveNumberMessage("amount")));

        if (states.isEmpty())
            return EntityState.valid;

        return EntityState.Invalid(states);
    }
}

