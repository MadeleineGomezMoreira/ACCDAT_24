package ui.methods;

import jakarta.inject.Inject;
import model.MedicalRecord;
import model.PrescribedMedication;
import services.MedicalRecordService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class SaveMedicalRecordWithPrescription {

    private final MedicalRecordService medicalRecordService;

    @Inject
    public SaveMedicalRecordWithPrescription(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    public void saveMedicalRecordWithPrescription() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter the patient's ID:");
        int patientId = sc.nextInt();
        sc.nextLine();

        System.out.printf("Please enter the doctor's ID:");
        int doctorId = sc.nextInt();
        sc.nextLine();

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

        PrescribedMedication firstMed = new PrescribedMedication(0, firstMedName, firstMedDosage, 0);
        PrescribedMedication secondMed = new PrescribedMedication(0, secondMedName, secondMedDosage, 0);

        MedicalRecord medRecord = new MedicalRecord(0, admissionDate, diagnosis, patientId, doctorId, List.of(firstMed, secondMed));

        medicalRecordService.save(medRecord).peek(i -> System.out.println("Medical record saved successfully!"))
                .peekLeft(error -> System.out.println("ERROR: " + error.getMessage()));

    }
}
