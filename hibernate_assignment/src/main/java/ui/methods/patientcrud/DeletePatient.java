package ui.methods.patientcrud;

import common.Constants;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import services.PatientService;

import java.util.Scanner;

public class DeletePatient {
    private final PatientService patientService;

    @Inject
    public DeletePatient(PatientService patientService) {
        this.patientService = patientService;
    }

    public void deletePatient() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the ID of the patient you would like to delete: ");
        int patientId = sc.nextInt();
        sc.nextLine();


        Either<AppError, Integer> result = patientService.deletePatient(patientId, false);

        if (result.isRight()) {
            System.out.println("Patient deleted successfully!");
        } else {
            String errorMessage = result.getLeft().getMessage();

            if (errorMessage.equals(Constants.PATIENT_STILL_HAS_APPOINTMENTS_ERROR)) {
                System.out.println(Constants.APPOINTMENT_DELETION_QUESTION_ERROR);
                String answer = sc.nextLine();

                if (answer.equalsIgnoreCase("Y")) {
                    patientService.deletePatient(patientId, true);
                    System.out.println("Patient deleted successfully!");
                } else {
                    System.out.println("Patient was not deleted. Operation canceled.");
                }
            } else {
                System.out.println("An error occurred: " + errorMessage);
            }

        }


    }
}
