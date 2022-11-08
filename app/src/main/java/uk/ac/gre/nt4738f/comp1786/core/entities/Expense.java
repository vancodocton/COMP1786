package uk.ac.gre.nt4738f.comp1786.core.entities;

import android.content.ContentValues;

import java.time.LocalDate;

public class Expense {
    public int id;
    public int tripId;
    public String type;
    public LocalDate time;
    public double amount;

    public Expense(int id, int tripId, String type, LocalDate time, double amount) {
        this.id = id;
        this.tripId = tripId;
        this.type = type;
        this.time = time;
        this.amount = amount;
    }

    public static Expense New(int tripId, String type, LocalDate time, double amount) {
        return new Expense(0, tripId, type, time, amount);
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        if (id != 0)
            cv.put("Id", id);

        if (tripId > 0)
            cv.put("TripId", tripId);

        if (type != null)
            cv.put("Type", type);

        if (time != null)
            cv.put("Time", time.toString());

        if (amount > 0)
            cv.put("Amount", amount);

        return cv;
    }
}