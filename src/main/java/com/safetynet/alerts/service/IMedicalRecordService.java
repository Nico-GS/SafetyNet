package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;

/**
 * Interface IMedicalRecordService
 */
public interface IMedicalRecordService {

    MedicalRecord createMedicalRecord(MedicalRecord newMedicalRecord);
    boolean updateMedicalRecord(MedicalRecord medicalRecord);
    boolean deleteMedicalRecord(String firstName, String lastName);

}
