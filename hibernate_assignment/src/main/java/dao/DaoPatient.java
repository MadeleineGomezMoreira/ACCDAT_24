package dao;

import io.vavr.control.Either;
import model.Patient;
import model.error.AppError;

import java.util.List;

public interface DaoPatient {
    Either<AppError, List<Patient>> getAll();

    Either<AppError, Patient> get(Patient patient);

    Either<AppError, Integer> save(Patient patient);

    Either<AppError, Integer> update(Patient patient);

    Either<AppError, Integer> delete(Patient patient, Boolean confirmation);
}
