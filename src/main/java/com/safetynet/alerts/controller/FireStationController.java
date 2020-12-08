package com.safetynet.alerts.controller;

import com.safetynet.alerts.constants.Constants;
import com.safetynet.alerts.dto.FireDTO;
import com.safetynet.alerts.dto.FloodDTO;
import com.safetynet.alerts.dto.PersonStationCounterDTO;
import com.safetynet.alerts.service.IFireStationService;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * FireStation controller class.
 */
@RestController
public class FireStationController {

    private static final Logger logger = LogManager.getLogger(FireStationController.class);

    @Autowired
    private IFireStationService fireStationService;

    /**
     * Create new FireStation number/address
     *
     * @param stationToCreate Map
     */
    @PostMapping("/firestation")
    public void addAddressForFirestation(
            @NotNull @RequestBody final Map<String, String> stationToCreate,
            final HttpServletResponse response) {

        boolean isAdded = fireStationService.addAddressForFirestation(stationToCreate);

        if (isAdded) {
            logger.info("OK 200 - addAddressForFireStation POST request "
                            + "-  FireStation number : {}, Address : {}",
                    stationToCreate.get("station"),
                    stationToCreate.get("address"));
            response.setStatus(Constants.STATUS_CREATED_201);
        } else {
            response.setStatus(Constants.ERROR_CONFLICT_409);
        }
    }

    /**
     * MAJ adresse existante FireStation
     *
     * @param mappingToUpdate Map
     */
    @PutMapping("/firestation")
    public void updateAddressForFireStation(
            @NotNull @RequestBody final Map<String, String> mappingToUpdate,
            final HttpServletResponse response) {

        boolean isUpdated = fireStationService.updateAddressForFireStation(mappingToUpdate);

        if (isUpdated) {
            logger.info("OK 200 - UpdateFireStation PUT request " + "-  FireStation number : {}, Address : {}",
                    mappingToUpdate.get("station"),
                    mappingToUpdate.get("address"));
            response.setStatus(Constants.STATUS_OK_200);
        } else {
            response.setStatus(Constants.ERROR_NOT_FOUND_404);
        }
    }

    /**
     * Delete address
     *
     * @param address String
     */
    @DeleteMapping("/firestation")
    public void deleteAddressForFireStation(
            @NotNull @RequestParam final String address,
            final HttpServletResponse response) {

        boolean isDeleted = fireStationService.deleteAddressForFireStation(address);

        if (isDeleted) {
            logger.info("OK 200 - DeleteFireStation DELETE request "
                    + "-  Address : {}", address);
            response.setStatus(Constants.STATUS_OK_200);
        } else {
            logger.info("404 NOT FOUND - Please verify the address");
            response.setStatus(Constants.ERROR_NOT_FOUND_404);
        }
    }

    /**
     * Check person per station and count adults / childrens
     *
     * @param stationNumber String
     * @return firestationDto
     */
    @GetMapping("/firestation")
    public PersonStationCounterDTO firestationNumber(
            @NotNull @RequestParam(value = "stationNumber") final String stationNumber,
            final HttpServletResponse response) {

        PersonStationCounterDTO firestationDto = fireStationService.firestationNumber(stationNumber);

        if (firestationDto.getPersonsStationList() != null) {
            logger.info("OK 200 - FireStationNumber GET request");
            response.setStatus(Constants.STATUS_OK_200);
        } else {
            logger.error("FAILED 404 - No FireStation found : {}. "
                    + "Check the station number", stationNumber);
            response.setStatus(Constants.ERROR_NOT_FOUND_404);
        }
        return firestationDto;
    }

    /**
     * Retrieve persons phone number covered by the station
     *
     * @param firestation the station number
     * @return phoneNumberList
     */
    @GetMapping("/phoneAlert")
    public List<String> phoneAlert(
            @NotNull @RequestParam(value = "firestation") final String firestation,
            final HttpServletResponse response) {
        List<String> phoneNumberList = fireStationService.phoneAlert(firestation);

        if (!phoneNumberList.isEmpty()) {
            logger.info("OK 200 - PhoneAlert GET request");
            response.setStatus(Constants.STATUS_OK_200);
        } else {
            logger.error("FAILED 404 - No FireStation founded for number : {}. "
                            + "Verify the station number entered.",
                    firestation);
            response.setStatus(Constants.ERROR_NOT_FOUND_404);
        }
        return phoneNumberList;
    }

    /**
     * Retrieve info of persons leaving at the address entered with the FireStation number
     *
     * @param address String
     * @return fireDtoPersonsList
     */
    @GetMapping("/fire")
    public List<FireDTO> fire(
            @NotNull @RequestParam(value = "address") final String address,
            final HttpServletResponse response) {

        List<FireDTO> fireDTOPersons = fireStationService.fire(address);

        if (!fireDTOPersons.isEmpty()) {
            logger.info("SUCCESS - Fire GET request");
            response.setStatus(Constants.STATUS_OK_200);
        } else {
            logger.error("FAILED - No address for : {}.", address);
            response.setStatus(Constants.ERROR_NOT_FOUND_404);
        }
        return fireDTOPersons;
    }

    /**
     * Flood - Infos : Name Age Phone MedicalRecords
     *
     * @param stations List String
     * @return flood as a list
     */
    @GetMapping("/flood/stations")
    public List<FloodDTO> flood(
            @NotNull @RequestParam(value = "stations") final List<String> stations,
            final HttpServletResponse response) {

        List<FloodDTO> flood = fireStationService.flood(stations);

        if (flood != null) {
            logger.info("OK 200 - Flood GET request");
            response.setStatus(Constants.STATUS_OK_200);
        } else {
            logger.error("FAILED 404 - Flood GET request");
            response.setStatus(Constants.ERROR_NOT_FOUND_404);
        }
        return flood;
    }

}
