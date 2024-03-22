package ui.methods;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Patient;
import model.error.AppError;
import services.PatientsService;

import java.util.List;

public class ShowAllPatients {

    private final PatientsService service;

    @Inject
    public ShowAllPatients(PatientsService service) {
        this.service = service;
    }

    public void showAllPatients() {
        Either<AppError, List<Patient>> either = service.getAll();

        if (either.isRight()) {
            System.out.println(either.get());
        } else if (either.isLeft()) {
            System.out.println(either.getLeft().getMessage());
        }
    }

}
