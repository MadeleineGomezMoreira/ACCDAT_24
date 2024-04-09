package ui.methods;

import jakarta.inject.Inject;
import services.PrescribedMedicationService;

import java.util.Scanner;

public class ShowPatientMedication {

    private final PrescribedMedicationService service;

    @Inject
    public ShowPatientMedication(PrescribedMedicationService service) {
        this.service = service;
    }

    public void showPatientMedication(int patientId) {

        if (patientId == 0) {
            System.out.println("Please input the patient's id:");
            Scanner sc = new Scanner(System.in);
            patientId = sc.nextInt();
            sc.nextLine();
        }
        service.getPrescribedMedicationByPatient(patientId).peek(System.out::println).peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));

    }

}
