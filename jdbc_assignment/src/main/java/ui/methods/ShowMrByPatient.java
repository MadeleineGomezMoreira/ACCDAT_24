package ui.methods;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MedicalRecord;
import model.error.AppError;
import services.MedicalRecordService;

import java.util.List;
import java.util.Scanner;

public class ShowMrByPatient {

    private final MedicalRecordService service;

    @Inject
    public ShowMrByPatient(MedicalRecordService service) {
        this.service = service;
    }

    public void showMrByPatient(int patientId) {

        if(patientId == 0) {
            System.out.println("Please input the patient's id:");
            Scanner sc = new Scanner(System.in);
            patientId = sc.nextInt();
            sc.nextLine();
        }
        service.getAllRecordsByPatient(patientId).peek(System.out::println).peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));

    }
}
