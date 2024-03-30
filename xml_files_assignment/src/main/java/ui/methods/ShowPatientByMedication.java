package ui.methods;

import jakarta.inject.Inject;
import services.PatientsService;

import java.util.Scanner;

public class ShowPatientByMedication {

    private final PatientsService service;

    @Inject
    public ShowPatientByMedication(PatientsService service) {
        this.service = service;
    }

    public void showPatientByMedication() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please input the medication name:");
        String medicationName = sc.nextLine();

        service.getPatientsByMedication(medicationName).peek(System.out::println).peekLeft(System.out::println);
    }
}
