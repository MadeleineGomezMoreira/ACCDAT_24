package ui.methods;

import jakarta.inject.Inject;
import model.MedicalRecord;
import model.PrescribedMedication;
import services.PrescribedMedicationService;

import java.util.Scanner;

public class SaveNewPrescription {

    private final PrescribedMedicationService prescribedMedicationService;

    @Inject
    public SaveNewPrescription(PrescribedMedicationService prescribedMedicationService) {
        this.prescribedMedicationService = prescribedMedicationService;
    }

    public void saveNewPrescription() {

        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter the medical record ID:");
        int medicalRecordId = sc.nextInt();
        sc.nextLine();

        System.out.println("Please enter the name of the medication:");
        String name = sc.nextLine();

        System.out.println("Please enter the dose of the medication (XXXmg/gr):");
        String dose = sc.nextLine();

        prescribedMedicationService.save(new PrescribedMedication(0, name, dose, medicalRecordId))
                .peek(prescribedMedication -> System.out.println("Prescribed medication saved successfully!"))
                .peekLeft(error -> System.out.println("ERROR: " + error.getMessage()));
    }
}
