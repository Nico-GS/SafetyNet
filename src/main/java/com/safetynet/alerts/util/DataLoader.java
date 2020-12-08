package com.safetynet.alerts.util;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.safetynet.alerts.model.AllInformations;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data loader JSON
 */
@Component
public class DataLoader {

    private static final Logger logger = LogManager.getLogger(DataLoader.class);
    private static int personCounter = 0;
    private static int fireStationCounter = 0;
    private static int medicalRecordCounter = 0;

    private DataLoader() {
    }

    /**
     * Data from json and convert in object
     *
     * @param dataFile String
     * @return allInformations
     */
    public static AllInformations readJsonFile(final String dataFile)
            throws IOException, NullPointerException {
        logger.debug("Initialization Data");
        byte[] byteArray = Files.readAllBytes(new File(dataFile).toPath());
        JsonIterator jsonIterator = JsonIterator.parse(byteArray);
        Any any = jsonIterator.readAny();

        // Persons
        Any anyPerson = any.get("persons");
        List<Person> persons = new ArrayList<>();
        Map<String, List<Person>> households = new HashMap<>();

        anyPerson.forEach(personJson -> {
            String firstName = personJson.get("firstName").toString();
            String lastName = personJson.get("lastName").toString();
            String address = personJson.get("address").toString();
            String city = personJson.get("city").toString();
            String zip = personJson.get("zip").toString();
            String phone = personJson.get("phone").toString();
            String email = personJson.get("email").toString();

            Person person = new Person(firstName, lastName, address, city, zip, phone, email);
            persons.add(person);
            personCounter++;

            // cache, remplace containskey ID, plus lisible
            List<Person> household = households.computeIfAbsent(address, temp -> new ArrayList<>());
            household.add(person);
        });

        // FireStations
        Any anyFireStation = any.get("firestations");
        Map<String, FireStation> firestations = new HashMap<>();
        anyFireStation.forEach(firestationJson -> {
            String id = firestationJson.get("station").toString();
            String address = firestationJson.get("address").toString();

            FireStation firestation = firestations.computeIfAbsent(id, FireStation::new);
            firestation.addAddress(address);
            fireStationCounter++;
        });

        // MedicalRecord
        Any anyMedicalRecord = any.get("medicalrecords");
        anyMedicalRecord.forEach(medicalRecordJson -> {
            String firstName = medicalRecordJson.get("firstName").toString();
            String lastName = medicalRecordJson.get("lastName").toString();
            String birthdate = medicalRecordJson.get("birthdate").toString();

            List<String> medications = new ArrayList<>();
            Any anyMedications = medicalRecordJson.get("medications");
            anyMedications.forEach(medicationJson -> medications.add(medicationJson.toString()));

            List<String> allergies = new ArrayList<>();
            Any anyAllergies = medicalRecordJson.get("allergies");
            anyAllergies.forEach(allergyJson -> allergies.add(allergyJson.toString()));

            // MedicalRecord aux bonnes personnes
            searchPerson(firstName, lastName, persons).setMedicalRecord(
                    new MedicalRecord(birthdate, medications, allergies));
            medicalRecordCounter++;
        });
        logger.debug("Loaded from a Json file : \r\n"
                + (personCounter) + " persons \r\n"
                + (fireStationCounter)
                + " firestations \r\n"
                + (medicalRecordCounter)
                + " medicalrecords");

        return new AllInformations(
                persons, firestations, households);
    }

    /**
     * Search person with first & last name
     *
     * @param firstName   String
     * @param lastName    String
     * @param personsList String
     * @return person
     */
    public static Person searchPerson(final String firstName,
                                      final String lastName, final List<Person> personsList) {
        return personsList.stream()
                .filter(person -> firstName.equals(person.getFirstName())
                        && lastName.equals(person.getLastName())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Name : "
                        + firstName.toUpperCase()
                        + " "
                        + lastName.toUpperCase()
                        + "not found"));
    }

}
