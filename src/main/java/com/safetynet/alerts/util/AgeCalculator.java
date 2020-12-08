package com.safetynet.alerts.util;

import com.safetynet.alerts.constants.Constants;
import com.safetynet.alerts.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * AgeCalculator class - Calcul if Minor or Adult
 */
public class AgeCalculator {

    private static final Logger logger = LogManager.getLogger(AgeCalculator.class);
    private static final int MINOR_UNDER = 18;

    private AgeCalculator() {
    }

    /**
     * AgeCalculation method
     *
     * @param birthDay String
     * @return age
     * @throws NullPointerException     NPE
     * @throws IllegalArgumentException IAE
     */
    public static int ageCalculation(final String birthDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE);
        LocalDate personsBirthdate = LocalDate.parse(birthDay, formatter);
        LocalDate currentDate = LocalDate.now();

        int age = Period.between(personsBirthdate, currentDate).getYears();

        if (personsBirthdate.isAfter(currentDate)) {
            logger.error("Person birthday invalid");
            throw new IllegalArgumentException("Error - BirthDay Unknown\n ");
        } else if (age == 0) {
            logger.debug("Baby before a year");
            age++;
        } else {
            return age;
        }
        return age;
    }

    /**
     * Calcul if person childs | adults
     *
     * @param person Person
     * @return isChild boolean
     */
    public static boolean isChild(final Person person) {
        boolean isChild = false;
        int personsAge = AgeCalculator.ageCalculation(person.getMedicalRecord().getBirthday());
        if (personsAge <= MINOR_UNDER) {
            isChild = true;
        }
        return isChild;
    }
}
