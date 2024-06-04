package ui.methods.patientcrud;

import common.Constants;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import org.bson.types.ObjectId;
import services.PatientService;

import java.util.HashMap;
import java.util.Scanner;

public class DeletePatient {
    private final PatientService patientService;

    @Inject
    public DeletePatient(PatientService patientService) {
        this.patientService = patientService;
    }

    public void deletePatient() {
        Scanner sc = new Scanner(System.in);

        Either<AppError, HashMap<Integer, ObjectId>> allPatientsIDs = patientService.getAllPatientsIDs();

        if (allPatientsIDs.isRight()) {
            allPatientsIDs.get().forEach((key, value) -> System.out.println(key + " - " + value));

            System.out.println("Enter the patient's id: (select the number associated to its object Id)");
            int id = sc.nextInt();
            sc.nextLine();

            ObjectId objectId = allPatientsIDs.get().get(id);

            Either<AppError, Integer> result = patientService.deletePatient(objectId, false);

            if (result.isRight()) {
                System.out.println("Patient deleted successfully!");
            } else {
                String errorMessage = result.getLeft().getMessage();

                if (errorMessage.equals(Constants.PATIENT_STILL_HAS_MEDICAL_RECORDS_ERROR)) {
                    System.out.println(Constants.MEDICAL_RECORD_DELETION_QUESTION_ERROR);
                    String answer = sc.nextLine();

                    if (answer.equalsIgnoreCase("Y")) {
                        patientService.deletePatient(objectId, true);
                        System.out.println("Patient deleted successfully!");
                    } else {
                        System.out.println("Patient was not deleted. Operation canceled.");
                    }
                } else {
                    System.out.println("An error occurred: " + errorMessage);
                }

            }

        } else {
            System.out.println(Constants.ERROR + allPatientsIDs.getLeft().getMessage());
        }

    }
}
