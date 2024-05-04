package ui.methods;

import jakarta.inject.Inject;
import services.PrescribedMedicationService;
import services.QueriesService;

import java.util.Scanner;

public class ShowPatientsWithSpecificPrescription {

    private final QueriesService service;

    @Inject
    public ShowPatientsWithSpecificPrescription(QueriesService service) {
        this.service = service;
    }

    public void showPatientMedication() {
        service.getQueryOne().peek(e -> System.out.println("THESE ARE THE PATIENTS THAT WERE PRESCRIBED 400MG IBUPROFEN: "+ "\n" + e.toString())).peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));
    }
}
