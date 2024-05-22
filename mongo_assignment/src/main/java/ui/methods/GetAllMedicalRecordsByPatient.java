package ui.methods;

import jakarta.inject.Inject;
import services.MedicalRecordService;

public class GetAllMedicalRecordsByPatient {

    private final MedicalRecordService medicalRecordService;

    @Inject
    public GetAllMedicalRecordsByPatient(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    public void getAllRecordsByPatient() {
        medicalRecordService.getRecordsByPatient()
                .peek(System.out::println)
                .peekLeft(e -> System.out.println("ERROR" + e.getMessage()));
    }
}
