package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PersonStationCounterDTO {

    private final List<PersonStationDTO> personsStationList;
    private final int totalAdultsNumber;
    private final int totalChildrenNumber;

}
