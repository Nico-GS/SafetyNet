package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.AllInformations;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

/**
 * PersonService units tests class.
 */
@WebMvcTest(PersonService.class)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private AllInformations allInformations;

    private static final String adultBirthday = "03/23/1993";
    private static final String childBirthday = "01/01/2020";
    private static MedicalRecord medicalRecordAdult;
    private static MedicalRecord medicalRecordChild;
    private static Person person1;
    private static Person person2;
    private static Person person3;

    @BeforeEach
    private void setUpPerTest() {
        List<String> medicationsList = new ArrayList<>();
        medicationsList.add("medication1");
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("allergies");
        medicalRecordAdult = new MedicalRecord(adultBirthday, medicationsList, allergiesList);
        medicalRecordChild = new MedicalRecord(childBirthday, medicationsList, allergiesList);
    }

    @Test
    @Tag("PersonInfo")
    @DisplayName("PersonInfo Existing Person")
    void personInfoExistingPerson() {
        List<Person> personsList = new ArrayList<>();
        person1 = new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6513", "drk@email.com", medicalRecordAdult);
        person2 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6512", "jaboyd@email.com", medicalRecordAdult);
        person3 = new Person("Eric", "Cadigan", "951 LoneTree Rd", "Culver",
                "97451", "841-874-7458", "gramps@email.com",
                medicalRecordAdult);

        personsList.add(person1);
        personsList.add(person2);
        personsList.add(person3);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        List<PersonInfoDTO> result = personService.personInfo("John", "Boyd");
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @Tag("PersonInfo")
    @DisplayName("PersonInfo Unknown Person")
    void personInfoUnknownPerson() {
        List<Person> personsList = new ArrayList<>();
        person1 = new Person("Maurice", "Maurice", "135 Rue de la Rue", "Culver", "97451",
                "111-222-3456", "testtest@email.com", medicalRecordAdult);
        person2 = new Person("Rabbit", "Boyd", "Rue du Bonjour la nuit", "Culver", "97451",
                "111-222-3456", "hrthed@email.com", medicalRecordAdult);
        person3 = new Person("Marcel", "Cadigan", "958 Rue du Blabla", "Culver",
                "97451", "111-222-3456", "testtest@email.com",
                medicalRecordAdult);
        personsList.add(person1);
        personsList.add(person2);
        personsList.add(person3);

        when(allInformations.getPersonsList()).thenReturn(personsList);
        List<PersonInfoDTO> result = personService.personInfo("Nicolas", "Gros");
        assertThat(result.isEmpty()).isTrue();
        assertThat(0).isZero();
        assertThat(result.toString().contains(person1.getFirstName())).isFalse();
        assertThat(result.toString().contains(person2.getFirstName())).isFalse();
        assertThat(result.toString().contains(person3.getFirstName())).isFalse();
    }

    @Test
    @Tag("CommunityEmail")
    @DisplayName("Valid City entry - CommunityEmail")
    void validCityEntryCommunityEmail() {
        List<Person> personsList = new ArrayList<>();
        person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6512", "jaboyd@email.com", medicalRecordAdult);
        person2 = new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6513", "drk@email.com", medicalRecordAdult);
        person3 = new Person("Eric", "Cadigan", "951 LoneTree Rd", "Culver",
                "97451", "841-874-7458", "gramps@email.com",
                medicalRecordAdult);
        Person otherCityPerson = new Person("Other", "Unknow", "Other city",
                "Other city", "00000", "111-111-111", "other@email.com");
        personsList.add(person1);
        personsList.add(person2);
        personsList.add(person3);
        personsList.add(otherCityPerson);

        when(allInformations.getPersonsList()).thenReturn(personsList);
        List<String> personsEmails = personService.communityEmail("Culver");
        assertThat(personsEmails.size()).isEqualTo(3);
        assertThat(personsEmails.get(0)).isEqualTo("jaboyd@email.com");
        assertThat(personsEmails.get(1)).isEqualTo("drk@email.com");
        assertThat(personsEmails.get(2)).isEqualTo("gramps@email.com");
    }

    @Test
    @Tag("CommunityEmail")
    @DisplayName("CommunityEmail - Bad city entry")
    public void givenCityEntry_whenUnknowCityEntry_thenReturnEmptyPersonsList() {
        List<Person> personsList = new ArrayList<>();
        person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6512", "jaboyd@email.com", medicalRecordAdult);
        person2 = new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6513", "drk@email.com", medicalRecordAdult);
        person3 = new Person("Eric", "Cadigan", "951 LoneTree Rd", "Culver",
                "97451", "841-874-7458", "gramps@email.com",
                medicalRecordAdult);
        personsList.add(person1);
        personsList.add(person2);
        personsList.add(person3);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        List<String> personsEmails = personService.communityEmail("Other city");
        assertThat(personsEmails.size()).isEqualTo(0);
        assertThat(personsEmails).isEmpty();
    }

    @Test
    @Tag("ChildAlert")
    @DisplayName("ChildAlert Childs & Adults with Same address")
    void childAlertChildsAndAdultsWithSameAddress() {

        // child 1 address ok
        person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6512", "jaboyd@email.com", medicalRecordChild);
        // adult address ok
        person2 = new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6513", "drk@email.com", medicalRecordAdult);
        // child 2 incorrect address
        person3 = new Person("Eric", "Cadigan", "951 LoneTree Rd", "Culver",
                "97451", "841-874-7458", "gramps@email.com",
                medicalRecordChild);

        // ajout meme personne dans foyer
        List<Person> householdMembersList = new ArrayList<>();
        householdMembersList.add(person1);
        householdMembersList.add(person2);

        Map<String, List<Person>> households = new HashMap<>();
        households.put(person1.getAddress(), householdMembersList);
        households.put(person2.getAddress(), householdMembersList);
        households.put(person3.getAddress(), householdMembersList);
        when(allInformations.getHouseholds()).thenReturn(households);
        List<ChildAlertDTO> result = personService.childAlert("1509 Culver St");
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    @Tag("ChildAlert")
    @DisplayName("ChildAlert Address without Child")
    void childAlertAddressWithoutChild() {
        person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6512", "jaboyd@email.com", medicalRecordAdult);
        person2 = new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6513", "drk@email.com", medicalRecordAdult);

        List<Person> householdMembersList = new ArrayList<>();
        householdMembersList.add(person1);
        householdMembersList.add(person2);
        Map<String, List<Person>> households = new HashMap<>();
        households.put(person1.getAddress(), householdMembersList);
        households.put(person2.getAddress(), householdMembersList);
        when(allInformations.getHouseholds()).thenReturn(households);
        List<ChildAlertDTO> result = personService.childAlert("1509 Culver St");
        assertThat(result.size()).isZero();
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @Tag("ChildAlert")
    @DisplayName("ChildAlert Unknown Address")
    void childAlertUnknownAddress() {
        person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6512", "jaboyd@email.com", medicalRecordChild);

        person2 = new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6513", "drk@email.com", medicalRecordAdult);

        List<Person> householdMembersList = new ArrayList<>();
        householdMembersList.add(person1);
        householdMembersList.add(person2);
        Map<String, List<Person>> households = new HashMap<>();
        households.put(person1.getAddress(), householdMembersList);
        households.put(person2.getAddress(), householdMembersList);
        when(allInformations.getHouseholds()).thenReturn(households);
        List<ChildAlertDTO> result = personService.childAlert("Unknow address");
        assertThat(result.size()).isZero();
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @Tag("POST")
    @DisplayName("Create New Person")
    void createNewPersonPostIsOk() {
        Map<String, String> personToCreate = new HashMap<String, String>();
        Person newPerson = new Person("New", "Person", "address", "City", "zip",
                "phone", "email");
        personToCreate.put("firstName", newPerson.getFirstName());
        personToCreate.put("lastName", newPerson.getLastName());
        personToCreate.put("address", newPerson.getAddress());
        personToCreate.put("city", newPerson.getCity());
        personToCreate.put("zip", newPerson.getZip());
        personToCreate.put("phone", newPerson.getPhone());
        personToCreate.put("email", newPerson.getEmail());

        List<Person> personsList = new ArrayList<>();
        when(allInformations.getPersonsList()).thenReturn(personsList);
        Map<String, List<Person>> households = new HashMap<>();
        when(allInformations.getHouseholds()).thenReturn(households);
        Person result = personService.createPerson(personToCreate);
        assertThat(result).isNotNull().hasFieldOrPropertyWithValue("firstName", newPerson.getFirstName())
                .hasFieldOrPropertyWithValue("lastName", newPerson.getLastName())
                .hasFieldOrPropertyWithValue("address", newPerson.getAddress())
                .hasFieldOrPropertyWithValue("city", newPerson.getCity())
                .hasFieldOrPropertyWithValue("zip", newPerson.getZip())
                .hasFieldOrPropertyWithValue("phone", newPerson.getPhone())
                .hasFieldOrPropertyWithValue("email", newPerson.getEmail());
        assertThat(personsList.contains(result)).isTrue();
        assertThat(households.containsKey(result.getAddress()));
        assertThat(households.get(result.getAddress()).contains(result));
    }

    @Test
    @Tag("POST")
    @DisplayName("Create Person - Verify if Person added to list")
    void createPersonAndVerifyIsPersonAddedToList() {
        Map<String, String> personToCreate = new HashMap<String, String>();
        Person householdPerson1 = new Person("New", "Person", "1509 Culver St",
                "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        personToCreate.put("firstName", householdPerson1.getFirstName());
        personToCreate.put("lastName", householdPerson1.getLastName());
        personToCreate.put("address", householdPerson1.getAddress());
        personToCreate.put("city", householdPerson1.getCity());
        personToCreate.put("zip", householdPerson1.getZip());
        personToCreate.put("phone", householdPerson1.getPhone());
        personToCreate.put("email", householdPerson1.getEmail());

        List<Person> personsList = new ArrayList<>();
        Person householdPerson2 = new Person("John", "Boyd", "1509 Culver St",
                "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        personsList.add(householdPerson2);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        Map<String, List<Person>> households = new HashMap<>();
        when(allInformations.getHouseholds()).thenReturn(households);
        Person result = personService.createPerson(personToCreate);
        assertThat(personsList.contains(result)).isTrue();
        assertThat(households.containsKey(result.getAddress()));
        assertThat(households.get(result.getAddress()).contains(result));
        assertThat(personsList.size()).isEqualTo(2);
    }

    @Test
    @Tag("POST")
    @DisplayName("Create Existing Person and Return List Unchanged")
    void createExistingPersonAndReturnListUnchanged() {
        Map<String, String> personToCreate = new HashMap<String, String>();
        Person newPerson = new Person("John", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6512", "jaboyd@email.com");
        personToCreate.put("firstName", newPerson.getFirstName());
        personToCreate.put("lastName", newPerson.getLastName());
        personToCreate.put("address", newPerson.getAddress());
        personToCreate.put("city", newPerson.getCity());
        personToCreate.put("zip", newPerson.getZip());
        personToCreate.put("phone", newPerson.getPhone());
        personToCreate.put("email", newPerson.getEmail());

        List<Person> personsList = new ArrayList<>();
        Person existingPerson = new Person("John", "Boyd", "1509 Culver St",
                "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        personsList.add(existingPerson);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        Person result = personService.createPerson(personToCreate);
        assertThat(personsList.contains(result)).isFalse();
        assertThat(personsList.contains(existingPerson)).isTrue();
        assertThat(personsList.size()).isEqualTo(1);
    }

    @Test
    @Tag("POST")
    @DisplayName("Create Person - Information Entered Null")
    void createPersonIfInformationEnteredIsNull() {
        Map<String, String> personToCreate = new HashMap<String, String>();
        Person personWithoutFirstname = new Person("", "Person", "address",
                "City", "zip", "phone", "email");
        personToCreate.put("firstName", personWithoutFirstname.getFirstName());
        personToCreate.put("lastName", personWithoutFirstname.getLastName());
        personToCreate.put("address", personWithoutFirstname.getAddress());
        personToCreate.put("city", personWithoutFirstname.getCity());
        personToCreate.put("zip", personWithoutFirstname.getZip());
        personToCreate.put("phone", personWithoutFirstname.getPhone());
        personToCreate.put("email", personWithoutFirstname.getEmail());

        assertThatExceptionOfType(StringIndexOutOfBoundsException.class).isThrownBy(() -> {
                    personService.createPerson(personToCreate);
                });
    }

    @Test
    @Tag("PUT")
    @DisplayName("UPDATE Existing Person")
    void updateExistingPerson() {
        List<Person> personsList = new ArrayList<>();
        person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6512", "jaboyd@email.com");
        personsList.add(person1);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        Person personToUpdate = new Person("John", "Boyd", "NEW ADDRESS", "city",
                "zip", "phone", "email");
        personService.updatePerson(personToUpdate);
        assertThat(personsList.size()).isEqualTo(1);
    }

    @Test
    @Tag("PUT")
    @DisplayName("UPDATE Unknown Person")
    void updateUnknownPerson() {
        List<Person> personsList = new ArrayList<>();
        person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6512", "jaboyd@email.com");
        personsList.add(person1);
        when(allInformations.getPersonsList()).thenReturn(personsList);

        Person personToUpdate = new Person("UNKNOW", "PERSON", "NEW ADDRESS",
                "city", "zip", "phone", "email");
        personService.updatePerson(personToUpdate);
        assertThat(personsList.size()).isEqualTo(1);
        assertThat(personsList.toString()).doesNotContain(personToUpdate.toString());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("DELETE Existing Person")
    void deleteExistingPerson() {
        List<Person> personsList = new ArrayList<>();
        person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6512", "jaboyd@email.com");
        person2 = new Person("Jacob", "Tenley", "1509 Culver St", "Culver",
                "97451", "841-874-6513", "drk@email.com");
        personsList.add(person1);
        personsList.add(person2);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        Person personToDelete = person1;
        personService.deletePerson(personToDelete);
        assertThat(personsList.size()).isEqualTo(1); // Check changed size list
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Delete Unknown Person")
    void deleteUnknownPerson() {
        List<Person> personsList = new ArrayList<>();
        person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451",
                "841-874-6512", "jaboyd@email.com");
        person2 = new Person("Jacob", "Tenley", "1509 Culver St", "Culver",
                "97451", "841-874-6513", "drk@email.com");
        personsList.add(person1);
        personsList.add(person2);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        Person personToDelete = new Person("Unknow", "Person", "1509 Culver St",
                "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        personService.deletePerson(personToDelete);
        assertThat(personsList.size()).isEqualTo(2); // Check unchanged size list
    }

}
