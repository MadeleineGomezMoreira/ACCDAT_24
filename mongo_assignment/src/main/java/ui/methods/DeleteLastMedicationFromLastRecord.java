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

public class DeleteLastMedicationFromLastRecord {

    private final PrescribedMedicationService prescribedMedicationService;
    private final PatientService patientService;

    @Inject
    public DeleteLastMedicationFromLastRecord(PrescribedMedicationService prescribedMedicationService, PatientService patientService) {
        this.prescribedMedicationService = prescribedMedicationService;
        this.patientService = patientService;
    }

    public void deleteMedication() {
        Scanner sc = new Scanner(System.in);

        Either<AppError, HashMap<Integer, ObjectId>> allPatientsIDs = patientService.getAllPatientsIDs();

        if (allPatientsIDs.isRight()) {
            allPatientsIDs.get().forEach((key, value) -> System.out.println(key + " - " + value));

            System.out.println("Enter the patient's id: (select the number associated to its object Id)");
            int id = sc.nextInt();
            sc.nextLine();

            ObjectId patientId = allPatientsIDs.get().get(id);

            prescribedMedicationService.deleteLastMedicationFromLastMedicalRecord(patientId)
                    .peek(i -> System.out.println("Last medication from last record deleted successfully!"))
                    .peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));

        } else {
            System.out.println(Constants.ERROR + allPatientsIDs.getLeft().getMessage());
        }


    }
}
