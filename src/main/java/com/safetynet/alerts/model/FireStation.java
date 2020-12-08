package com.safetynet.alerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * FireStation class
 */
@AllArgsConstructor
@Getter
@Setter
public class FireStation {

    private final Set<String> addresses = new HashSet<>();
    private String station;

    public void addAddress(final String newAddress) {
        addresses.add(newAddress);
    }

}
