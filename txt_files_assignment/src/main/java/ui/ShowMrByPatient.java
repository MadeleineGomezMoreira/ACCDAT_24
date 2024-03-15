package ui;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MedicalRecord;
import model.error.AppError;
import services.MedicalRecordsService;

import java.util.List;

public class ShowMrByPatient {

    private final MedicalRecordsService service;

    @Inject
    public ShowMrByPatient(MedicalRecordsService service) {
        this.service = service;
    }

    public void showMrByPatient() {
        Either<AppError, List<MedicalRecord>> either = service.getAll(1);

        if (either.isRight()) {
            System.out.println(either.get());
        } else if (either.isLeft()) {
            System.out.println(either.getLeft().getMessage());
        }
    }
}
