package ui.methods;

import common.Constants;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import model.mongo.MedicalRecord;
import model.mongo.PrescribedMedication;
import org.bson.types.ObjectId;
import services.DoctorService;
import services.MedicalRecordService;
import services.PatientService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class SaveMedicalRecordWithPrescription {

    private final MedicalRecordService medicalRecordService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Inject
    public SaveMedicalRecordWithPrescription(MedicalRecordService medicalRecordService, DoctorService doctorService, PatientService patientService) {
        this.medicalRecordService = medicalRecordService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public void saveMedicalRecordWithPrescription() {
        Scanner sc = new Scanner(System.in);

        Either<AppError, HashMap<Integer, ObjectId>> allPatientsIDs = patientService.getAllPatientsIDs();
        Either<AppError, HashMap<Integer, ObjectId>> allDoctorIDs = doctorService.getAllDoctorsIDs();

        try {

            if (allPatientsIDs.isRight() && allDoctorIDs.isRight()) {
                allPatientsIDs.get().forEach((key, value) -> System.out.println(key + " - " + value));

                System.out.println("Enter the patient's id: (select the number associated to its object Id)");
                int patientId = sc.nextInt();
                sc.nextLine();

                ObjectId patientObjectId = allPatientsIDs.get().get(patientId);

                allDoctorIDs.get().forEach((key, value) -> System.out.println(key + " - " + value));

                System.out.println("Enter the doctor's id: (select the number associated to its object Id)");
                int doctorId = sc.nextInt();
                sc.nextLine();

                ObjectId doctorObjectId = allDoctorIDs.get().get(doctorId);


                System.out.println("Please enter the diagnosis:");
                String diagnosis = sc.nextLine();

                System.out.println("Please enter the admission's date (YYYY-MM-DD):");
                String date = sc.nextLine();
                LocalDate admissionDate = LocalDate.parse(date);

                System.out.println("Please enter the first medication's name:");
                String firstMedName = sc.nextLine();

                System.out.println("Please enter the first medication's dosage:");
                String firstMedDosage = sc.nextLine();

                System.out.println("Please enter the second medication's name:");
                String secondMedName = sc.nextLine();

                System.out.println("Please enter the second medication's dosage:");
                String secondMedDosage = sc.nextLine();

                PrescribedMedication firstMed = PrescribedMedication.builder()
                        .name(firstMedName)
                        .dose(firstMedDosage)
                        .build();
                PrescribedMedication secondMed = PrescribedMedication.builder()
                        .name(secondMedName)
                        .dose(secondMedDosage)
                        .build();

                MedicalRecord medRecord = MedicalRecord.builder()
                        .patientId(patientObjectId)
                        .doctorId(doctorObjectId)
                        .diagnosis(diagnosis)
                        .admissionDate(admissionDate)
                        .prescribedMedication(List.of(firstMed, secondMed))
                        .build();

                medicalRecordService.save(medRecord).peek(i -> System.out.println("Medical record saved successfully!"))
                        .peekLeft(error -> System.out.println("ERROR: " + error.getMessage()));

            } else {
                System.out.println(Constants.ERROR + allPatientsIDs.getLeft().getMessage());
            }
        }   catch (Exception e) {
            System.out.println(e.getClass() + " ERROR: You did not choose the correct number associated to the doctor or patient's ID.");
        }
    }
}
