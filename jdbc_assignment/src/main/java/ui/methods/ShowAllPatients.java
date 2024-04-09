package ui.methods;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Patient;
import model.dto.PatientDTO;
import model.error.AppError;
import services.PatientService;

import java.util.List;

public class ShowAllPatients {

    private final PatientService service;

    @Inject
    public ShowAllPatients(PatientService service) {
        this.service = service;
    }

    public void showAllPatients() {
        service.getAllWithAmountPaid().peek(System.out::println).peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));
    }

}
