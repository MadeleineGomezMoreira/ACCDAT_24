package ui.methods;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MedicalRecord;
import model.error.AppError;
import services.MedicalRecordsService;

import java.util.List;
import java.util.Scanner;

public class ShowMrByPatient {

    private final MedicalRecordsService service;

    @Inject
    public ShowMrByPatient(MedicalRecordsService service) {
        this.service = service;
    }

    public void showMrByPatient() {

        Scanner sc = new Scanner(System.in);

        System.out.println("Please input the patient's id:");

        int patientId = sc.nextInt();
        sc.nextLine();

        Either<AppError, List<MedicalRecord>> either = service.getAll(patientId);

        if (either.isRight()) {
            System.out.println(either.get());
        } else if (either.isLeft()) {
            System.out.println(either.getLeft().getMessage());
        }
    }
}
