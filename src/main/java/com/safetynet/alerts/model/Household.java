package com.safetynet.alerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Household class
 */
@AllArgsConstructor
@Getter
@Setter
public class Household {

    private List<Person> householdComposition;

}
