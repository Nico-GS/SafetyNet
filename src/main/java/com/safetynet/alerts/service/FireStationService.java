package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.model.AllInformations;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.util.AgeCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

/**
 * FireStationService class
 */
@Service
public class FireStationService implements IFireStationService {

    private static final Logger logger = LogManager.getLogger(FireStationService.class);

    /**
     * Informations from entitiesInfos
     */
    @Autowired
    private AllInformations allInformations;

    /**
     * Create new firestation number/address
     *
     * @param firestationMappingToCreate Map
     * @return isAdded boolean
     */
    public boolean addAddressForFirestation(final Map<String, String> firestationMappingToCreate) {

        try {
            Map<String, FireStation> allFirestationsMapping = allInformations.getFirestations();
            String newAddress = firestationMappingToCreate.get("address");
            FireStation firestationRecovered = allFirestationsMapping.get(firestationMappingToCreate.get("station"));

            for (Entry<String, FireStation> entry : allFirestationsMapping.entrySet()) {
                FireStation firestationsNumber = entry.getValue();

                // Si même adresse déjà enregistrée
                if (firestationsNumber.getAddresses().toString().contains(newAddress)) {
                    logger.error("Address enter : {} already exist ", firestationMappingToCreate.values());
                    return false;
                }
            }
            firestationRecovered.addAddress(newAddress);
            return true;
        } catch (NullPointerException np) {
            throw new NullPointerException("NPE - Verify station number" + np);
        }
    }

    /**
     * Update existing address for other existing firestation
     *
     * @param firestationMapCreate Map
     */
    public boolean updateAddressForFireStation(final Map<String, String> firestationMapCreate) {

        try {
            Map<String, FireStation> allFirestationsMapping = allInformations.getFirestations();
            String address = firestationMapCreate.get("address");
            FireStation firestationNumberRecovered = allFirestationsMapping.get(firestationMapCreate.get("station"));

            for (Entry<String, FireStation> entry : allFirestationsMapping.entrySet()) {
                FireStation firestationsNumber = entry.getValue();

                // 1 - Verifie adresse existante
                // 2 - Delete addresse attribuee a une autre station
                // 3 - MAJ adresse avec l'autre station
                if (firestationsNumber.getAddresses().toString().contains(address)) {
                    firestationsNumber.getAddresses().remove(address);
                    firestationNumberRecovered.addAddress(address);
                    return true;
                }
            }
            logger.error("Address enter : {} does not exist.",
                    firestationMapCreate.values());
            return false;
        } catch (NullPointerException np) {
            throw new NullPointerException("NPE - Please verify the number station " + np);
        }
    }

    /**
     * Delete existing address / update the address/station
     *
     * @param address String
     * @return isDeleted boolean
     */
    public boolean deleteAddressForFireStation(final String address) {
        Map<String, FireStation> allFirestationsMapping = allInformations
                .getFirestations();

        for (Entry<String, FireStation> entry : allFirestationsMapping
                .entrySet()) {
            FireStation firestationsNumber = entry.getValue();

            if (firestationsNumber.getAddresses().toString().contains(address)) {
                firestationsNumber.getAddresses().remove(address);
                return true;
            }
        }
        logger.error("Address entered : {} not exist.", address);
        return false;
    }

    /**
     * Retrieve persons covered by the station / calculate number adults-children.
     *
     * @param stationNumber String
     */
    public PersonStationCounterDTO firestationNumber(final String stationNumber) {

        int totalAdultsNumber = 0;
        int totalChildrenNumber = 0;

        Map<String, FireStation> allFirestationsMapping = allInformations.getFirestations();
        Set<String> addressesRecovered = new HashSet<>();
        for (Entry<String, FireStation> entry : allFirestationsMapping.entrySet()) {
            FireStation firestation = entry.getValue();

            if (firestation.getStation().equals(stationNumber)) {
                addressesRecovered = firestation.getAddresses();
            }
        }
        // verifie
        if (addressesRecovered.isEmpty()) {
            return new PersonStationCounterDTO(null,
                    0, 0);
        }
        List<Person> allPersonsList = allInformations.getPersonsList();
        List<PersonStationDTO> personsUnderResponsibility = new ArrayList<>();

        for (Person person : allPersonsList) {
            if (addressesRecovered.contains(person.getAddress())) {
                // Ajout person
                PersonStationDTO personStationDto = new PersonStationDTO(
                        person.getFirstName(), person.getLastName(),
                        person.getAddress(), person.getPhone());
                personsUnderResponsibility.add(personStationDto);
                // Count childs & adults
                if (!AgeCalculator.isChild(person)) {
                    totalAdultsNumber++;
                } else {
                    totalChildrenNumber++;
                }
            }
        }
        return new PersonStationCounterDTO(
                personsUnderResponsibility, totalAdultsNumber,
                totalChildrenNumber);
    }

    /**
     * Retrieve phone number by FireStation
     *
     * @param fireStation String
     * @return phoneNumberList
     */
    public List<String> phoneAlert(final String fireStation) {
        Map<String, FireStation> allFirestationsMapping = allInformations.getFirestations();
        Set<String> addressesRecovered = new HashSet<>();

        for (Entry<String, FireStation> entry : allFirestationsMapping.entrySet()) {
            FireStation firestationToRecover = entry.getValue();
            if (firestationToRecover.getStation().equals(fireStation)) {
                addressesRecovered = firestationToRecover.getAddresses();
            }
        }
        List<Person> allPersonsList = allInformations.getPersonsList();
        List<String> phoneNumberList = new ArrayList<>();

        for (Person person : allPersonsList) {
            if (addressesRecovered.contains(person.getAddress())) {
                phoneNumberList.add(person.getPhone());
            }
        }
        return phoneNumberList;
    }

    /**
     * Retrieve persons at the address by firestation number
     *
     * @param address String
     * @return fireDtoPersonsList
     */
    public List<FireDTO> fire(final String address) {

        Map<String, FireStation> allFirestationsMapping = allInformations.getFirestations();
        List<FireDTO> fireDtoPersonsList = new ArrayList<>();

        for (Entry<String, FireStation> entry : allFirestationsMapping.entrySet()) {
            FireStation firestation = entry.getValue();
            if (firestation.getAddresses().contains(address)) {
                String stationNumber = firestation.getStation();

                // recupere personne par household
                Map<String, List<Person>> households = allInformations.getHouseholds();
                for (Entry<String, List<Person>> entryset : households.entrySet()) {
                    String householdAddress = entryset.getKey();
                    if (!address.equals(householdAddress)) {
                        continue;
                    }
                    List<Person> householdMembersList = entryset.getValue();
                    for (Person person : householdMembersList) {
                        FireDTO fireDtoPerson = new FireDTO(stationNumber,
                                person.getFirstName(), person.getLastName(),
                                person.getMedicalRecord().getAge(),
                                person.getPhone(),
                                person.getMedicalRecord().getMedications(),
                                person.getMedicalRecord().getAllergies());
                        fireDtoPersonsList.add(fireDtoPerson);
                    }
                }
            }
        }
        return fireDtoPersonsList;
    }

    /**
     * Retrieve households served by FireStation - List of information : Name, Age, Phone, MedicalRecord
     *
     * @param stations List<String>
     * @return floodDtoList
     */
    public List<FloodDTO> flood(final List<String> stations) {

        List<FloodDTO> floodDtoList = new ArrayList<>();
        Map<String, FireStation> allFireStations = allInformations.getFirestations();
        List<Person> allPersonsList = allInformations.getPersonsList();

        for (String station : stations) {
            // recupere toute les adresse des firestations
            Set<String> addressesRecovered = new HashSet<>();
            for (Entry<String, FireStation> entry : allFireStations.entrySet()) {
                FireStation firestationToRecover = entry.getValue();
                if (firestationToRecover.getStation().contains(station)) {
                    addressesRecovered = firestationToRecover.getAddresses();
                    break;
                }
            }
            if (addressesRecovered.isEmpty()) {
                logger.error("No station found {}", station);
                return null;
            }
            // regroupe les personnes allant à la même adresse de FireStation
            Map<AddressDTO, List<PersonFloodDTO>> householdDTO = new HashMap<>();
            for (Person person : allPersonsList) {

                if (addressesRecovered.contains(person.getAddress())) {
                    PersonFloodDTO floodPerson = new PersonFloodDTO(
                            person.getFirstName(), person.getLastName(),
                            person.getPhone(),
                            person.getMedicalRecord().getAge(),
                            person.getMedicalRecord().getMedications(),
                            person.getMedicalRecord().getAllergies());
                    AddressDTO addressDTO = new AddressDTO(person.getAddress(), person.getCity(), person.getZip());

                    boolean isSameHouse = false;
                    for (Entry<AddressDTO, List<PersonFloodDTO>> entry : householdDTO.entrySet()) {
                        if (entry.getKey().getAddress().equals(addressDTO.getAddress()) && entry.getKey().getCity()
                                .equals(addressDTO.getCity()) && entry.getKey().getZip()
                                .equals(addressDTO.getZip())) {
                            entry.getValue().add(floodPerson);
                            isSameHouse = true;
                        }
                    }
                    if (!isSameHouse) {
                        householdDTO.put(addressDTO, new ArrayList<>());
                        for (Entry<AddressDTO, List<PersonFloodDTO>> entry : householdDTO.entrySet()) {
                            if (entry.getKey().getAddress().equals(addressDTO.getAddress())
                                    && entry.getKey().getCity().equals(addressDTO.getCity())
                                    && entry.getKey().getZip().equals(addressDTO.getZip())) {
                                entry.getValue().add(floodPerson);
                            }
                        }
                    }
                }
            }
            List<HouseholdsFloodDTO> householdsFloodList = new ArrayList<>();
            for (Entry<AddressDTO, List<PersonFloodDTO>> entry : householdDTO.entrySet()) {
                HouseholdsFloodDTO householdsFloodDTO = new HouseholdsFloodDTO(entry.getKey(), entry.getValue());
                householdsFloodList.add(householdsFloodDTO);
            }
            FloodDTO flood = new FloodDTO(station, householdsFloodList);
            floodDtoList.add(flood);
        }
        return floodDtoList;
    }
}
