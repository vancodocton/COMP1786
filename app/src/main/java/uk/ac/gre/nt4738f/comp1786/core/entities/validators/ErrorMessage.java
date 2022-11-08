package uk.ac.gre.nt4738f.comp1786.core.entities.validators;

import org.jetbrains.annotations.NotNull;

public class ErrorMessage {
    public final String propertyName;
    public final String message;

    public ErrorMessage(@NotNull String propertyName, @NotNull String template) {
        this.propertyName = propertyName;
        message = String.format(template, propertyName);
    }

    public static ErrorMessage InvalidPropertyMessage(String propertyName) {
        String template = "Invalid property '%s'";
        return new ErrorMessage(propertyName, template);
    }

    public static ErrorMessage NullPropertyMessage(String propertyName) {
        String template = "Empty property '%s'";
        return new ErrorMessage(propertyName, template);
    }

    public static ErrorMessage NullOrEmptyPropertyMessage(String propertyName) {
        String template = "Empty or blank property '%s'";
        return new ErrorMessage(propertyName, template);
    }

    public static ErrorMessage InvalidPositiveNumberMessage(String propertyName) {
        String template = "Invalid Invalid Positive Number Property '%s'";
        return new ErrorMessage(propertyName, template);
    }
}
