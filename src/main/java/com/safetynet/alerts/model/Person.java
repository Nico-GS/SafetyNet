package com.safetynet.alerts.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Person class
 */
@Getter
@Setter
public class Person {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
    private MedicalRecord medicalRecord;

    public Person() {
    }

    /**
     * Constructor used to convert json data in java object.
     */
    public Person(final String personsFirstName, final String personsLastName,
                  final String personsAddress, final String personsCity,
                  final String personsZip, final String personsPhoneNumber,
                  final String personsEmail) {
        this.firstName = personsFirstName;
        this.lastName = personsLastName;
        this.address = personsAddress;
        this.city = personsCity;
        this.zip = personsZip;
        this.phone = personsPhoneNumber;
        this.email = personsEmail;
    }

    /**
     * Constructor with MedicalRecord
     */
    public Person(final String personsFirstName, final String personsLastName,
                  final String personsAddress, final String personsCity,
                  final String personsZip, final String personsPhoneNumber,
                  final String personsEmail,
                  final MedicalRecord personsMedicalRecord) {
        this.firstName = personsFirstName;
        this.lastName = personsLastName;
        this.address = personsAddress;
        this.city = personsCity;
        this.zip = personsZip;
        this.phone = personsPhoneNumber;
        this.email = personsEmail;
        this.medicalRecord = personsMedicalRecord;
    }

}
