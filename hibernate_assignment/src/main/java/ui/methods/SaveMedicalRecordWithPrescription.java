package ui.methods;

import jakarta.inject.Inject;
import services.MedicalRecordService;

public class SaveMedicalRecordWithPrescription {

    private final MedicalRecordService medicalRecordService;

    @Inject
    public SaveMedicalRecordWithPrescription(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }
}
