package com.safetynet.alerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Retrieve all the informations
 */
@AllArgsConstructor
@Getter
@Setter
public class AllInformations {

    private List<Person> personsList;
    private Map<String, FireStation> firestations;
    private Map<String, List<Person>> personsPerHousehold;

    public Map<String, List<Person>> getHouseholds() {
        return personsPerHousehold;
    }

    public void setPersonList(final List<Person> persons) {
        this.personsList = persons;
    }

}
