package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PersonFloodDTO {

    private final String firstName;
    private final String lastName;
    private final String phone;
    private final int age;
    private final List<String> medications;
    private final List<String> allergies;

}
