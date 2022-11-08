package uk.ac.gre.nt4738f.comp1786.core.entities.validators;

import org.jetbrains.annotations.NotNull;

public class PropertyState {
    public static PropertyState Valid = new PropertyState(true, null);
    public final boolean IsValid;
    public final String Message;

    private PropertyState(boolean isValid, String errorMessage) {
        IsValid = isValid;
        Message = errorMessage;
    }

    public static PropertyState Invalid(@NotNull String errorMessage) {
        return new PropertyState(false, errorMessage);
    }

    public static PropertyState Invalid(@NotNull ErrorMessage errorMessage) {
        return new PropertyState(false, errorMessage.message);
    }
}
