package dao;

import io.vavr.control.Either;
import model.Patient;
import model.error.AppError;

import java.util.List;

public interface DaoPatients {
    Either<AppError, List<Patient>> getAll();

    //get patient by id
    Either<AppError, Patient> get(Patient patient);

    Either<AppError, Integer> delete(Patient patient);
}
