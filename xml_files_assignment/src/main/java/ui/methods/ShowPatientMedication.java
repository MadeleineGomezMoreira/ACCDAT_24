package ui.methods;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.PrescribedMedication;
import model.error.AppError;
import services.PrescribedMedicationService;

import java.util.List;
import java.util.Scanner;

public class ShowPatientMedication {

    private final PrescribedMedicationService service;

    @Inject
    public ShowPatientMedication(PrescribedMedicationService service) {
        this.service = service;
    }

    public void showPatientMedication() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Please input the patient's id:");

        int patientId = sc.nextInt();
        sc.nextLine();

        Either<AppError, List<PrescribedMedication>> either = service.getPatientMedication(patientId);

        if (either.isRight()) {
            List<PrescribedMedication> medicationList = either.get();
            System.out.println("Patient's medication:");
            System.out.println(medicationList);
        } else {
            System.out.println(either.getLeft().getMessage());
        }


    }

}
