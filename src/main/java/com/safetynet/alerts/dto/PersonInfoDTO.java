package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PersonInfoDTO {

    private final String firstName;
    private final String lastName;
    private final int age;
    private final String address;
    private final String city;
    private final String zip;
    private final String email;
    private final List<String> medications;
    private final List<String> allergies;

}
