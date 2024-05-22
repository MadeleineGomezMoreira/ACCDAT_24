package services;

import io.vavr.control.Either;
import model.Patient;
import model.error.AppError;

import java.util.List;

public interface PatientService {
    Either<AppError, List<Patient>> getAllPatients();

    Either<AppError, Patient> getPatientById(int id);

    Either<AppError, Integer> updatePatient(Patient patient);

    Either<AppError, Integer> savePatient(Patient patient);

    Either<AppError, Integer> deletePatient(int id, Boolean confirmation);
}
