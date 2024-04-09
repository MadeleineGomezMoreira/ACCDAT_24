package dao;

import io.vavr.control.Either;
import model.Patient;
import model.error.AppError;

import java.util.List;

public interface DaoPatient {
    Either<AppError, List<Patient>> getAll();

    //get all by medication
    Either<AppError, List<Patient>> getAll(Patient patient);

    //get the patient with the most medical records
    Either<AppError, Patient> get();

    //get patient by username
    Either<AppError, Patient> get(String username);

    Either<AppError, Integer> delete(Patient patient, boolean confirmed);
}
