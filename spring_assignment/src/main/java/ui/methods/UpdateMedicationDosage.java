package ui.methods;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.PrescribedMedication;
import model.error.AppError;
import services.PrescribedMedicationService;

import java.util.List;
import java.util.Scanner;

public class UpdateMedicationDosage {

    private final PrescribedMedicationService service;

    @Inject
    public UpdateMedicationDosage(PrescribedMedicationService service) {
        this.service = service;
    }

    public void updateMedicationDosage() {
        Scanner sc = new Scanner(System.in);

        System.out.println("List of all medication prescribed to patients:");

        Either<AppError, List<PrescribedMedication>> getAllMedication = service.getAllPrescribedMedication();

        if (getAllMedication.isRight()) {
            List<PrescribedMedication> medicationList = getAllMedication.get();
            medicationList.forEach(System.out::println);

            System.out.println("Please enter the id of the medication you want to update:");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.println("Please enter the new dose of the medication (format: XXXmg | XXgr):");
            String newDose = sc.nextLine();

            PrescribedMedication prescribedMedication = new PrescribedMedication(id, newDose);

            service.updateMedicationDose(prescribedMedication).peek(e -> System.out.println("Medication updated successfully!")).
                    peekLeft(e -> System.out.println("ERROR:" + e.getMessage()));
        } else {
            System.out.println(getAllMedication.getLeft().getMessage());
        }


    }
}
