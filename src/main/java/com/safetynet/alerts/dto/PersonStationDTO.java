package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PersonStationDTO {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String phoneNumber;

}
