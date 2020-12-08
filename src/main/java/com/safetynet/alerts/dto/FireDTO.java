package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FireDTO {

    private final String stationNumber;
    private final String firstName;
    private final String lastName;
    private final int age;
    private final String phoneNumber;
    private final List<String> medications;
    private final List<String> allergies;
}
