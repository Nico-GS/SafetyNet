package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class HouseholdsFloodDTO {

    private final AddressDTO address;
    private final List<PersonFloodDTO> personsList;

}
