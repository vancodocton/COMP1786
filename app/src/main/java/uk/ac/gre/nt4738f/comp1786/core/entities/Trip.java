package uk.ac.gre.nt4738f.comp1786.core.entities;

import android.content.ContentValues;

import java.time.LocalDate;

public class Trip {
    public int id;

    public String name;

    public String destination;

    public LocalDate date;

    public Boolean isRiskAssessment;

    public String description;

    public Trip(int id, String name, String destination, LocalDate date, Boolean isRiskAssessment, String description) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.date = date;
        this.isRiskAssessment = isRiskAssessment;
        this.description = description;
    }

    public static Trip New(String name, String destination, LocalDate date, Boolean isRiskAssessment, String description) {
        return new Trip(0, name, destination, date, isRiskAssessment, description);
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        if (id != 0)
            cv.put("Id", id);

        if (name != null)
            cv.put("Name", name);

        if (destination != null)
            cv.put("Destination", destination);

        if (date != null)
            cv.put("Date", date.toString());

        if (isRiskAssessment != null)
            cv.put("IsRiskAssessment", isRiskAssessment);

        if (description != null)
            cv.put("Description", description);
        return cv;
    }
}

