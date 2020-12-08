package com.safetynet.alerts.controller;

import com.safetynet.alerts.constants.Constants;
import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.IPersonService;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Person Controller
 */
@RestController
public class PersonController {

    private static final Logger logger = LogManager.getLogger(PersonController.class);

    @Autowired
    private IPersonService personService;

    /**
     * Create a new Person
     *
     * @param personToCreate Map
     */
    @PostMapping("/person")
    public void createPerson(
            @NotNull @RequestBody final Map<String, String> personToCreate,
            final HttpServletResponse response) {
        Person personsCreated = personService.createPerson(personToCreate);

        if (personsCreated != null) {
            logger.info("OK 201 - Create Person POST request for {} {}",
                    personsCreated.getFirstName(),
                    personsCreated.getLastName());
            response.setStatus(Constants.STATUS_CREATED_201);
        } else {
            logger.info("FAILED 409 - CONFLICT");
            response.setStatus(Constants.ERROR_CONFLICT_409);
        }
    }

    /**
     * Update Person
     *
     * @param personToUpdate Person
     */
    @PutMapping("/person")
    public void updatePerson(@NotNull @RequestBody final Person personToUpdate,
                             final HttpServletResponse response) {

        boolean isUpdated = personService.updatePerson(personToUpdate);

        if (isUpdated) {
            logger.info("SUCCESS - UpdatePerson PUT request");
            response.setStatus(Constants.STATUS_OK_200);
        } else {
            logger.error("404 - No person found : {} {}",
                    personToUpdate.getFirstName(),
                    personToUpdate.getLastName());
            response.setStatus(Constants.ERROR_NOT_FOUND_404);
        }
    }

    /**
     * Delete person.
     *
     * @param personToDelete Person
     */
    @DeleteMapping("/person")
    public void deletePerson(@NotNull @RequestBody final Person personToDelete,
                             final HttpServletResponse response) {

        boolean isDeleted = personService.deletePerson(personToDelete);

        if (isDeleted) {
            logger.info("OK 200 - DeletePerson DELETE request");
            response.setStatus(Constants.STATUS_OK_200);
        } else {
            logger.error("404 - No person found : {} {}",
                    personToDelete.getFirstName(),
                    personToDelete.getLastName());
            response.setStatus(Constants.ERROR_NOT_FOUND_404);
        }
    }

    /**
     * Return persons informations with same last name
     *
     * @param firstName String
     * @param lastName  String
     * @return personInfos list
     */
    @GetMapping("/personInfo")
    public List<PersonInfoDTO> personInfo(@RequestParam final String firstName,
                                          @NotNull @RequestParam final String lastName,
                                          final HttpServletResponse response) {

        logger.debug("GET request received for personInfos: {}", lastName);
        List<PersonInfoDTO> personInfos = personService.personInfo(firstName, lastName);

        if (!personInfos.isEmpty()) {
            logger.info("OK 200 - personInfos GET request");
            response.setStatus(Constants.STATUS_OK_200);
        } else {
            logger.error("404 FAILED - No person found with LastName : {}", lastName);
            response.setStatus(Constants.ERROR_NOT_FOUND_404);
        }
        return personInfos;
    }

    /**
     * Return composition of households if address entered contains child
     *
     * @param address String
     * @return childAlert
     */
    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildAlert(
            @NotNull @RequestParam(value = "address") final String address,
            final HttpServletResponse response) {

        logger.debug("GET request received for getChildAlert : {}", address);
        List<ChildAlertDTO> childAlert = personService.childAlert(address);

        if (!childAlert.isEmpty()) {
            logger.info("OK 200 - ChildAlert GET request");
            response.setStatus(Constants.STATUS_OK_200);
        } else {
            logger.error("404 FAILED - No household with child found : {}", address);
            response.setStatus(Constants.ERROR_NOT_FOUND_404);
        }
        return childAlert;
    }

    /**
     * Return email of persons in the city entered
     *
     * @param city String
     * @return List of emails (communityEmail)
     */
    @GetMapping("/communityEmail")
    public List<String> getCommunityEmail(
            @NotNull @RequestParam(value = "city") final String city,
            final HttpServletResponse response) {

        List<String> communityEmail = personService.communityEmail(city);

        if (!communityEmail.isEmpty()) {
            logger.info("OK 200 - CommunityEmail GET request");
            response.setStatus(Constants.STATUS_OK_200);
        } else {
            logger.error("404 FAILED - Not found : {}", city);
            String notFounded = "Not found : " + city;
            communityEmail.add(notFounded);
            response.setStatus(Constants.ERROR_NOT_FOUND_404);
        }
        return communityEmail;
    }

}
