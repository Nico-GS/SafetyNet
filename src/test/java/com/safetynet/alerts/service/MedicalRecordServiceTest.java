package com.safetynet.alerts.service;

import com.safetynet.alerts.model.AllInformations;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * MedicalRecordService units tests class
 */
@WebMvcTest(MedicalRecordService.class)
@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @Mock
    private AllInformations allInformations;
    private static final String BIRTHDAY_ADULT = "23/03/1993";
    private static final MedicalRecord medicalRecordAdult;
    private static final List<String> listMedications;
    private static final List<String> listAllergies;

    static {
        listMedications = new ArrayList<>();
        listMedications.add("MedicationAdd1");
        listAllergies = new ArrayList<>();
        listAllergies.add("AllergiesAdd");
        medicalRecordAdult = new MedicalRecord(BIRTHDAY_ADULT, listMedications, listAllergies);
    }

    @Test
    @Tag("POST")
    @DisplayName("CREATE Person OK Without MedicalRecord")
    void createPersonOkWithoutMedicalRecord() {
        // GIVEN
        List<Person> personsList = new ArrayList<>();
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6512", "jaboyd@email.com",
                medicalRecordAdult);
        Person person2 = new Person("Jacob", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6513", "drk@email.com");
        personsList.add(person1);
        personsList.add(person2);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        List<String> medicationsList = new ArrayList<>();
        medicationsList.add("medication1");
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("allergies");
        MedicalRecord newMedicalRecord = new MedicalRecord("Jacob", "Boyd",
                BIRTHDAY_ADULT, medicationsList, allergiesList);
        // WHEN
        MedicalRecord result = medicalRecordService.createMedicalRecord(newMedicalRecord);
        // THEN
        assertThat(result).isNotNull();
    }

    @Test
    @Tag("POST")
    @DisplayName("CREATE Person ERROR with already MedicalRecord existing")
    void createPersonErrorWithAlreadyMedicalRecordExisting() {
        List<Person> personsList = new ArrayList<>();
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6512", "jaboyd@email.com",
                medicalRecordAdult);
        Person person2 = new Person("Jacob", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6513", "drk@email.com");
        personsList.add(person1);
        personsList.add(person2);
        when(allInformations.getPersonsList()).thenReturn(personsList);

        List<String> medicationsList = new ArrayList<>();
        medicationsList.add("medication1");
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("allergies");

        MedicalRecord newMedicalRecord = new MedicalRecord("John", "Boyd",
                BIRTHDAY_ADULT, medicationsList, allergiesList);
        MedicalRecord result = medicalRecordService
                .createMedicalRecord(newMedicalRecord);
        assertThat(result).isNull();
    }

    @Test
    @Tag("PUT")
    @DisplayName("UPDATE Person ERROR Unknown Person")
    void updatePersonErrorBecauseUnknownPerson() {
        List<Person> personsList = new ArrayList<>();
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6512", "jaboyd@email.com",
                medicalRecordAdult);
        personsList.add(person1);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        List<String> medicationsList = new ArrayList<>();
        medicationsList.add("newMedic:100mg");
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("newAllergy");
        MedicalRecord medicalRecordUpdated = new MedicalRecord("Unknow", "Person",
                "01/01/1903", medicationsList, allergiesList);
        boolean result = medicalRecordService.updateMedicalRecord(medicalRecordUpdated);
        assertThat(result).isFalse();
        assertThat(person1.getMedicalRecord().getBirthday()).isEqualTo("23/03/1993");
        assertThat(person1.getMedicalRecord().getAllergies().toString()).hasToString("[AllergiesAdd]");
        assertThat(person1.getMedicalRecord().getMedications().toString()).hasToString("[MedicationAdd1]");
    }

    @Test
    @Tag("POST")
    @DisplayName("CREATE Person FAILED Person is Null")
    void createPersonFailedBecausePersonIsNull() {
        List<Person> personsList = new ArrayList<>();
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6512", "jaboyd@email.com",
                medicalRecordAdult);
        Person person2 = new Person("Jacob", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6513", "drk@email.com");
        personsList.add(person1);
        personsList.add(person2);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        List<String> medicationsList = new ArrayList<>();
        medicationsList.add("MedicationAdd1");
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("AllergiesAdd");
        MedicalRecord newMedicalRecord = new MedicalRecord("Nicolas", "Gros",
                BIRTHDAY_ADULT, medicationsList, allergiesList);
        MedicalRecord result = medicalRecordService.createMedicalRecord(newMedicalRecord);
        assertThat(result).isNull();
    }

    @Test
    @Tag("PUT")
    @DisplayName("UPDATE Person OK With MedicalRecord")
    void updatePersonOkWithMedicalRecord() {
        List<Person> personsList = new ArrayList<>();
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6512", "jaboyd@email.com",
                medicalRecordAdult);
        personsList.add(person1);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        List<String> medicationsList = new ArrayList<>();
        medicationsList.add("newDose:1000mg");
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("AllergiesAdd");
        MedicalRecord medicalRecordUpdated = new MedicalRecord("John", "Boyd",
                "06/06/1066", medicationsList, allergiesList);
        boolean result = medicalRecordService.updateMedicalRecord(medicalRecordUpdated);
        assertThat(result).isTrue();
        assertThat(person1.getFirstName()).isEqualTo("John");
        assertThat(person1.getMedicalRecord().getBirthday()).isEqualTo("06/06/1066");
        assertThat(person1.getMedicalRecord().getAllergies().toString()).isEqualTo("[AllergiesAdd]");
        assertThat(person1.getMedicalRecord().getMedications().toString()).isEqualTo("[newDose:1000mg]");
    }

    @Test
    @Tag("DELETE")
    @DisplayName("DELETE Person OK With MedicalRecord")
    void deletePersonOkWithMedicalRecord() {
        List<Person> personsList = new ArrayList<>();
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6512", "jaboyd@email.com",
                medicalRecordAdult);
        personsList.add(person1);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        boolean result = medicalRecordService.deleteMedicalRecord("John", "Boyd");
        assertThat(result).isTrue();
        assertThat(person1.getFirstName()).isEqualTo("John");
        assertThat(person1.getMedicalRecord()).isNull();
    }

    @Test
    @Tag("DELETE")
    @DisplayName("DELETE Person ERROR Without MedicalRecord")
    void deletePersonErrorWithoutMedicalRecord() {
        List<Person> personsList = new ArrayList<>();
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6512", "jaboyd@email.com");
        personsList.add(person1);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        boolean result = medicalRecordService.deleteMedicalRecord("John", "Boyd");
        assertThat(result).isFalse();
        assertThat(person1.getFirstName()).isEqualTo("John");
        assertThat(person1.getMedicalRecord()).isNull();
    }

    @Test
    @Tag("DELETE")
    @DisplayName("DELETE Person ERROR Unknown Person")
    void deletePersonErrorUnknownPerson() {
        List<Person> personsList = new ArrayList<>();
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver",
                "97451", "841-874-6512", "jaboyd@email.com",
                medicalRecordAdult);
        personsList.add(person1);
        when(allInformations.getPersonsList()).thenReturn(personsList);
        boolean result = medicalRecordService.deleteMedicalRecord("Nicolas", "Gros");
        assertThat(result).isFalse();
    }
}
