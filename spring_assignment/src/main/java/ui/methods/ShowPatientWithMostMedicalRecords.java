package ui.methods;

import jakarta.inject.Inject;
import services.PatientService;

public class ShowPatientWithMostMedicalRecords {

    private final PatientService service;

    @Inject
    public ShowPatientWithMostMedicalRecords(PatientService service) {
        this.service = service;
    }

    public void showPatientWithMostMedicalRecords() {
        service.getPatientWithMostMedicalRecords().peek(System.out::println).peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));
    }
}
