package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChildAlertDTO {

    private final String firstName;
    private final String lastName;
    private final int age;

    public ChildAlertDTO(final int personsAge, final String personFirstName,
                         final String personLastName) {
        this.age = personsAge;
        this.firstName = personFirstName;
        this.lastName = personLastName;
    }

}
