package ui.methods;

import jakarta.inject.Inject;
import services.PatientsService;

import java.util.Scanner;

public class ShowPatientByMedicationXML {

    private final PatientsService service;

    @Inject
    public ShowPatientByMedicationXML(PatientsService service) {
        this.service = service;
    }

    public void showPatientByMedication() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please input the medication name:");
        String medicationName = sc.nextLine();

        service.getPatientsByMedication(medicationName).peek(System.out::println).peekLeft(System.out::println);
    }
}
