package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireDTO;
import com.safetynet.alerts.dto.FloodDTO;
import com.safetynet.alerts.dto.PersonStationCounterDTO;

import java.util.List;
import java.util.Map;

/**
 * Interface IFireStationService
 */
public interface IFireStationService {

    List<String> phoneAlert(String firestation);

    List<FireDTO> fire(String address);

    List<FloodDTO> flood(List<String> stations);

    boolean addAddressForFirestation(Map<String, String> firestationToCreate);

    boolean updateAddressForFireStation(Map<String, String> firestationToUpdate);

    boolean deleteAddressForFireStation(String address);

    PersonStationCounterDTO firestationNumber(String stationNumber);


}
