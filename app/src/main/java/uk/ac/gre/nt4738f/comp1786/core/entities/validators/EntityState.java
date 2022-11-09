package uk.ac.gre.nt4738f.comp1786.core.entities.validators;

import java.util.ArrayList;

public class EntityState {
    public final static EntityState valid = new EntityState(true, null);

    public final boolean isValid;
    public final ArrayList<PropertyState> propertyStates;

    private EntityState(boolean isValid, ArrayList<PropertyState> propertyStates) {
        this.isValid = isValid;
        this.propertyStates = propertyStates;
    }

    public static EntityState Invalid(ArrayList<PropertyState> states) {
        return new EntityState(false, states);
    }

    public String getSingleErrorMessage() {
        if (this.isValid)
            return null;
        StringBuilder builder = new StringBuilder().append("Error:");
        for (PropertyState propertyState : this.propertyStates) {
            if (!propertyState.IsValid) {
                builder.append("\n");
                builder.append(propertyState.Message);
            }
        }
        return builder.toString();
    }
}
