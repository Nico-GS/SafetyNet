package com.safetynet.alerts.model;

import com.safetynet.alerts.util.AgeCalculator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * MedicalRecord class
 */
@AllArgsConstructor
@Getter
@Setter
public class MedicalRecord {

    private String firstName;
    private String lastName;
    private String birthday;
    private List<String> medications;
    private List<String> allergies;

    public MedicalRecord() {
    }

    /**
     * @param personBirthday   String
     * @param personMedication List
     * @param personAllergie   List
     */
    public MedicalRecord(final String personBirthday,
                         final List<String> personMedication,
                         final List<String> personAllergie) {
        this.birthday = personBirthday;
        this.medications = personMedication;
        this.allergies = personAllergie;
    }

    /**
     * @return person age
     */
    public int getAge() {
        return AgeCalculator.ageCalculation(this.birthday);
    }

}
