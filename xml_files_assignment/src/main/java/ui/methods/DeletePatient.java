package ui.methods;

import common.Constants;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import services.PatientsService;

import java.util.Scanner;

public class DeletePatient {

    private final PatientsService service;

    @Inject
    public DeletePatient(PatientsService service) {
        this.service = service;
    }

    public void deletePatient() {
        boolean confirmed = false;
        Scanner sc = new Scanner(System.in);

        System.out.println("Please input the patient's id:");

        int patientId = sc.nextInt();
        sc.nextLine();

        Either<AppError,Integer> either = service.delete(patientId, confirmed);




        if (either.isRight()) {
            System.out.println("Patient deleted successfully!");
        } else if (either.isLeft()) {

            if(either.getLeft().getMessage().equals(Constants.PATIENT_HAS_MEDICATION_ASSOCIATED_TO_MEDICAL_RECORDS_ERROR)){
                System.out.println(either.getLeft().getMessage());
                System.out.println("Please confirm the deletion of the patient (Y/N).");
                String input = sc.nextLine();

                if (input.equals("Y")) {
                    confirmed = true;
                    either = service.delete(patientId, confirmed);
                    if (either.isRight()) {
                        System.out.println("Patient deleted successfully!");
                    } else {
                        System.out.println(either.getLeft().getMessage());
                    }
                } else {
                    System.out.println("Goodbye!");
                }
            } else{
                System.out.println(either.getLeft());
            }

        }
    }
}
