package uk.ac.gre.nt4738f.comp1786.ui;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public interface IUpdateDate {
    void updateDatePicker(int viewDatePickerId, @NotNull LocalDate date);
}
