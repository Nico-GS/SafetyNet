package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.Person;

import java.util.List;
import java.util.Map;

/**
 * Interface IPersonService
 */
public interface IPersonService {

    Person createPerson(Map<String, String> personToCreate);
    boolean updatePerson(Person personToUpdate);
    boolean deletePerson(Person personToDelete);
    List<PersonInfoDTO> personInfo(String firstName, String lastName);
    List<ChildAlertDTO> childAlert(String address);
    List<String> communityEmail(String city);
}
