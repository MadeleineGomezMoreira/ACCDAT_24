package ui.methods;

import jakarta.inject.Inject;
import services.MedicalRecordService;

public class ShowRecordsByPatient {

    private final MedicalRecordService medicalRecordService;

    @Inject
    public ShowRecordsByPatient(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    public void showRecordsWithMedication() {
        medicalRecordService.getAllRecordsByPatient()
                .peek(records -> records.forEach(System.out::println))
                .peekLeft(error -> System.out.println("ERROR: " + error.getMessage()));
    }
}
