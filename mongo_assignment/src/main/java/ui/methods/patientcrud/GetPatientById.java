package ui.methods.patientcrud;

import common.Constants;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import org.bson.types.ObjectId;
import services.PatientService;

import java.util.HashMap;
import java.util.Scanner;

public class GetPatientById {

    private final PatientService patientService;

    @Inject
    public GetPatientById(PatientService patientService) {
        this.patientService = patientService;
    }

    public void getPatientById() {

        Scanner sc = new Scanner(System.in);

        Either<AppError, HashMap<Integer, ObjectId>> allPatientsIDs = patientService.getAllPatientsIDs();

        if (allPatientsIDs.isRight()) {
            allPatientsIDs.get().forEach((key, value) -> System.out.println(key + " - " + value));

            System.out.println("Enter the patient's id: (select the number associated to its object Id)");
            int id = sc.nextInt();
            sc.nextLine();

            ObjectId objectId = allPatientsIDs.get().get(id);

            patientService.getPatientById(objectId)
                    .peek(System.out::println)
                    .peekLeft(appError -> System.out.println(Constants.ERROR + appError.getMessage()));
        } else {
            System.out.println(Constants.ERROR + allPatientsIDs.getLeft().getMessage());
        }
    }
}
