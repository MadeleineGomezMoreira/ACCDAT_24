package ui.methods;

import common.Constants;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import model.hibernate.PrescribedMedicationEntity;
import model.mongo.PrescribedMedication;
import org.bson.types.ObjectId;
import services.MedicalRecordService;
import services.PatientService;
import services.PrescribedMedicationService;

import java.util.HashMap;
import java.util.Scanner;

public class SaveNewPrescriptionToLastMedicalRecord {

    private final PrescribedMedicationService prescribedMedicationService;
    private final MedicalRecordService medicalRecordService;
    private final PatientService patientService;

    @Inject
    public SaveNewPrescriptionToLastMedicalRecord(PrescribedMedicationService prescribedMedicationService, MedicalRecordService medicalRecordService, PatientService patientService) {
        this.prescribedMedicationService = prescribedMedicationService;
        this.medicalRecordService = medicalRecordService;
        this.patientService = patientService;
    }

    public void saveNewPrescription() {

        Scanner sc = new Scanner(System.in);

        Either<AppError, HashMap<Integer, ObjectId>> allPatientsIDs = patientService.getAllPatientsIDs();

        if (allPatientsIDs.isRight()) {
            allPatientsIDs.get().forEach((key, value) -> System.out.println(key + " - " + value));

            System.out.println("Enter the patient's id: (select the number associated to its object Id)");
            int id = sc.nextInt();
            sc.nextLine();

            ObjectId patientId = allPatientsIDs.get().get(id);

            System.out.println("Please enter the name of the medication:");
            String name = sc.nextLine();

            System.out.println("Please enter the dose of the medication (XXXmg/gr):");
            String dose = sc.nextLine();

            PrescribedMedication newMedication = PrescribedMedication.builder()
                    .patientId(patientId)
                    .name(name)
                    .dose(dose)
                    .build();

            prescribedMedicationService.save(newMedication)
                    .peek(prescribedMedication -> System.out.println("Prescribed medication saved successfully!"))
                    .peekLeft(appError -> System.out.println(Constants.ERROR + appError.getMessage()));

        } else {
            System.out.println(Constants.ERROR + allPatientsIDs.getLeft().getMessage());
        }
    }
}
