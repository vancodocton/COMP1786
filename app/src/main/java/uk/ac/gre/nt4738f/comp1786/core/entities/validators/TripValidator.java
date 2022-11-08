package uk.ac.gre.nt4738f.comp1786.core.entities.validators;


import java.util.ArrayList;

import uk.ac.gre.nt4738f.comp1786.core.entities.Trip;

public class TripValidator implements IEntityValidator<Trip> {
    public EntityState Validate(Trip trip) {
        ArrayList<PropertyState> states = new ArrayList<>();
        if (trip.id < 0)
            states.add(PropertyState.Invalid(ErrorMessage.InvalidPropertyMessage("Id")));

        if (trip.name == null || trip.name.trim().isEmpty())
            states.add(PropertyState.Invalid(ErrorMessage.InvalidPropertyMessage("Name")));

        if (trip.destination == null || trip.destination.trim().isEmpty())
            states.add(PropertyState.Invalid(ErrorMessage.NullOrEmptyPropertyMessage("Destination")));

        if (trip.date == null)
            states.add(PropertyState.Invalid(ErrorMessage.NullPropertyMessage("Date")));

        if (trip.isRiskAssessment == null)
            states.add(PropertyState.Invalid(ErrorMessage.NullPropertyMessage("Is Risk Assessment")));

        if (states.isEmpty())
            return EntityState.valid;
        return EntityState.Invalid(states);
    }
}

