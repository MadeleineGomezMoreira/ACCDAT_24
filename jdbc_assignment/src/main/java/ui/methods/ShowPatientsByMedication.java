package ui.methods;

import jakarta.inject.Inject;
import services.PatientService;

import java.util.Scanner;

public class ShowPatientsByMedication {

    private final PatientService service;

    @Inject
    public ShowPatientsByMedication(PatientService service) {
        this.service = service;
    }

    public void showPatientByMedication() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please input the medication name:");
        String medicationName = sc.nextLine();

        service.getAllByMedication(medicationName).peek(System.out::println).peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));
    }
}
