package ui.methods;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MedicalRecord;
import model.PrescribedMedication;
import model.error.AppError;
import services.MedicalRecordService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class SaveRecordWithMedication {

    private final MedicalRecordService service;

    @Inject
    public SaveRecordWithMedication(MedicalRecordService service) {
        this.service = service;
    }

    public void saveRecordWithMedication() {

        Scanner sc = new Scanner(System.in);

        //show the error message if the user inputs non existent doctor/patient id

        System.out.println("Please input the patient's id:");
        int patientId = sc.nextInt();
        sc.nextLine();
        System.out.println("Please input the doctor's id:");
        int doctorId = sc.nextInt();
        sc.nextLine();

        List<PrescribedMedication> medicationList = List.of(
                new PrescribedMedication(0, "Ibuprofen", "10mg", 0),
                new PrescribedMedication(0, "Benadryl", "20gm", 0)
        );

        MedicalRecord medicalRecord =
                new MedicalRecord(
                        0,
                        LocalDate.now(),
                        "headache",
                        patientId,
                        doctorId,
                        medicationList
                );

        Either<AppError, Integer> either = service.saveMedicalRecordWithMedication(medicalRecord);

        if (either.isRight()) {
            System.out.println("Medical record added successfully!");
            System.out.println(medicalRecord);

        } else if (either.isLeft()) {
            System.out.println(either.getLeft().getMessage());
        }


    }
}
