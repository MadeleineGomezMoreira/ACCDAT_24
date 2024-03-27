package services;

import io.vavr.control.Either;
import model.Patient;
import model.error.AppError;

import java.util.List;

public interface PatientsService {
    Either<AppError, List<Patient>> getAll();

    Either<AppError, Integer> delete(int patientId, Boolean confirmed);
}
