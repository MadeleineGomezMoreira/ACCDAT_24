package ui.methods;

import jakarta.inject.Inject;
import model.PrescribedMedication;
import services.PrescribedMedicationService;

import java.util.Scanner;

public class SaveMedicationToNewestRecord {

    private final PrescribedMedicationService service;

    @Inject
    public SaveMedicationToNewestRecord(PrescribedMedicationService service) {
        this.service = service;
    }

    public void saveMedicationToNewestRecord() {

        System.out.println("Please input the patient's id:");
        Scanner sc = new Scanner(System.in);
        int patientId = sc.nextInt();
        sc.nextLine();

        System.out.println("Please input the medication's name:");
        String medicationName = sc.nextLine();
        System.out.println("Please input the medication's dose (FORMAT: XXXmg / XXgr):");
        String medicationDose = sc.nextLine();

        PrescribedMedication medication = new PrescribedMedication(patientId, medicationName, medicationDose, 0);


        service.saveMedicationToNewestRecord(medication).peek(e -> System.out.println("Medication successfully added!")).peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));

    }
}
