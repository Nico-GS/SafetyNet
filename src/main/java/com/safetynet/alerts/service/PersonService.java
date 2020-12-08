package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.AllInformations;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.util.AgeCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * Person service class
 */
@Service
public class PersonService implements IPersonService {

    private static final Logger logger = LogManager.getLogger(PersonService.class);
    /**
     * Retrieve persons informations from AllInformations
     */
    @Autowired
    private AllInformations allInformations;

    /**
     * Create person / Update HouseHolds
     *
     * @param personToCreate Map
     * @return Person newPerson
     */
    public Person createPerson(final Map<String, String> personToCreate) {

        // premiere lettre prénom / nom / ville en MAJ
        Person newPerson = new Person(personToCreate.get("firstName").substring(0, 1)
                .toUpperCase()
                + personToCreate.get("firstName").substring(1).toLowerCase(),
                personToCreate.get("lastName").substring(0, 1).toUpperCase()
                        + personToCreate.get("lastName").substring(1).toLowerCase(),
                personToCreate.get("address"), personToCreate.get("city").substring(0, 1)
                .toUpperCase() + personToCreate.get("city").substring(1).toLowerCase(),
                personToCreate.get("zip"),
                personToCreate.get("phone"),
                personToCreate.get("email"));
        List<Person> personsList = allInformations.getPersonsList();

        // verifie pour eviter de creer une personne deja existante
        for (Person existingPerson : personsList) {
            if (existingPerson.getFirstName().equals(newPerson.getFirstName())
                    && existingPerson.getLastName()
                    .equals(newPerson.getLastName())) {
                logger.error("Error create person : {} {}, "
                                + "person already exist",
                        newPerson.getFirstName(), newPerson.getLastName());
                return null;
            }
        }
        personsList.add(newPerson);

        // Update households
        Map<String, List<Person>> households = allInformations
                .getHouseholds();
        List<Person> newPersonHousehold = households.computeIfAbsent(
                newPerson.getAddress(), temp -> new ArrayList<>());
        newPersonHousehold.add(newPerson);
        return newPerson;
    }

    /**
     * Update person.
     *
     * @param personToUpdate Person
     * @return isUpdated boolean
     */
    public boolean updatePerson(final Person personToUpdate) {
        List<Person> personsList = allInformations.getPersonsList();
        boolean isUpdated = false;

        for (Person existingPerson : personsList) {
            if (existingPerson.getFirstName().equals(personToUpdate.getFirstName())
                    && existingPerson.getLastName()
                    .equals(personToUpdate.getLastName())) {
                existingPerson.setAddress(personToUpdate.getAddress());
                existingPerson.setCity(personToUpdate.getCity());
                existingPerson.setZip(personToUpdate.getZip());
                existingPerson.setPhone(personToUpdate.getPhone());
                existingPerson.setEmail(personToUpdate.getEmail());
                allInformations.setPersonsList(personsList);
                isUpdated = true;
            }
        }
        return isUpdated;
    }

    /**
     * Delete person
     *
     * @param personToDelete Person
     * @return isDeleted boolean
     */
    public boolean deletePerson(final Person personToDelete) {
        List<Person> personsList = allInformations.getPersonsList();
        boolean isDeleted = false;

        for (Iterator<Person> iter = personsList.iterator(); iter.hasNext(); ) {
            Person existingPerson = iter.next();

            if (existingPerson.getFirstName().matches(Pattern.quote(personToDelete.getFirstName()))
                    && existingPerson.getLastName().matches(
                    Pattern.quote(personToDelete.getLastName()))) {
                iter.remove();
                isDeleted = true;
            }
        }
        return isDeleted;
    }

    /**
     * Return persons informations for same last name
     *
     * @param firstName String
     * @param lastName  String
     * @return personsInfosList List
     */
    public List<PersonInfoDTO> personInfo(final String firstName, final String lastName) {
        logger.debug("Request PersonInfo");
        List<PersonInfoDTO> personsInfosList = new ArrayList<>();
        List<Person> personsList = allInformations.getPersonsList();

        for (Person person : personsList) {
            if (person.getLastName().equals(lastName)) {
                PersonInfoDTO personsInfos = new PersonInfoDTO(
                        person.getFirstName(), person.getLastName(),
                        person.getMedicalRecord().getAge(), person.getAddress(),
                        person.getCity(), person.getZip(), person.getEmail(),
                        person.getMedicalRecord().getMedications(),
                        person.getMedicalRecord().getAllergies());
                personsInfosList.add(personsInfos);
            }
        }
        logger.debug("Request PersonInfo OK");
        return personsInfosList;
    }

    /**
     * Return households if address contains childs
     *
     * @param address String
     * @return childAlert List
     */
    public List<ChildAlertDTO> childAlert(final String address) {
        logger.debug("Request ChildAlert");
        List<ChildAlertDTO> childAlert = new ArrayList<>();
        Map<String, List<Person>> households = allInformations.getHouseholds();
        boolean isWithChilds = false;

        for (Entry<String, List<Person>> entry : households.entrySet()) {
            String householdAddress = entry.getKey();
            if (!address.equals(householdAddress)) {
                continue;
            }
            List<Person> householdMembersList = entry.getValue();
            for (Person person : householdMembersList) {
                // si child
                if (AgeCalculator.isChild(person)) {
                    int childAge = AgeCalculator.ageCalculation(person.getMedicalRecord().getBirthday());
                    ChildAlertDTO child = new ChildAlertDTO(childAge, person.getFirstName(), person.getLastName());
                    childAlert.add(child);
                    isWithChilds = true;

                    // si adulte
                } else if (!AgeCalculator.isChild(person)) {
                    int adultAge = AgeCalculator.ageCalculation(
                            person.getMedicalRecord().getBirthday());
                    ChildAlertDTO adultHouseholdMember = new ChildAlertDTO(adultAge,
                            person.getFirstName(), person.getLastName());
                    childAlert.add(adultHouseholdMember);
                }
            }
        }
        // liste vide si pas d'enfants
        if (!isWithChilds) {
            childAlert.clear();
        }
        return childAlert;
    }

    /**
     * Create list persons for a city
     *
     * @param city String
     * @return personsEmail List
     */
    public List<String> communityEmail(final String city) {

        List<String> personsEmail = new ArrayList<>();
        List<Person> personsList = allInformations.getPersonsList();

        for (Person person : personsList) {
            if (person.getCity().equals(city)) {
                personsEmail.add(person.getEmail());
            }
        }
        return personsEmail;
    }
}
