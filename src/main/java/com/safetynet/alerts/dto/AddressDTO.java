package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddressDTO {

    private final String address;
    private final String city;
    private final String zip;

}
