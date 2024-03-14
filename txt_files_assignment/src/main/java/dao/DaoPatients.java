package dao;

import io.vavr.control.Either;
import model.Patient;
import model.error.AppError;

import java.util.List;

public interface DaoPatients {
    Either<AppError, List<Patient>> getAll();

    Either<AppError, Integer> delete(Patient patient);
}
