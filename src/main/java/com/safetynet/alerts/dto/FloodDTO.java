package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FloodDTO {

    private final String station;
    private final List<HouseholdsFloodDTO> householdsList;

    public String getStationNumber() {
        return station;
    }
}
