package ui.methods;

import common.Constants;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import org.bson.types.ObjectId;
import services.PatientService;
import services.PrescribedMedicationService;

import java.util.HashMap;
import java.util.Scanner;

public class GetMedicationFromSpecificPatient {

    private final PatientService patientService;
    private final PrescribedMedicationService prescribedMedicationService;

    @Inject
    public GetMedicationFromSpecificPatient(PatientService patientService, PrescribedMedicationService prescribedMedicationService) {
        this.patientService = patientService;
        this.prescribedMedicationService = prescribedMedicationService;
    }

    public void getMedicationFromSpecificPatient() {

        Scanner sc = new Scanner(System.in);

        Either<AppError, HashMap<Integer, ObjectId>> allPatientsIDs = patientService.getAllPatientsIDs();

        if (allPatientsIDs.isRight()) {
            allPatientsIDs.get().forEach((key, value) -> System.out.println(key + " - " + value));

            System.out.println("Enter the patient's id: (select the number associated to its object Id)");
            int id = sc.nextInt();
            sc.nextLine();

            ObjectId objectId = allPatientsIDs.get().get(id);

            prescribedMedicationService.getMedicationFromSpecificPatient(objectId)
                    .peek(prescribedMedicationDTO -> System.out.println(prescribedMedicationDTO.toString()))
                    .peekLeft(appError -> System.out.println(Constants.ERROR + appError.getMessage()));
        } else {
            System.out.println(Constants.ERROR + allPatientsIDs.getLeft().getMessage());
        }

    }

}
