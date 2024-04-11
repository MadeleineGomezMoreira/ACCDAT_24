package services;

import io.vavr.control.Either;
import model.Patient;
import model.dto.PatientDTO;
import model.error.AppError;

import java.util.List;

public interface PatientService {
    Either<AppError, List<PatientDTO>> getAllWithAmountPaid();

    Either<AppError, Patient> getPatientWithMostMedicalRecords();

    Either<AppError, Patient> getPatientByUsername(String username);

    Either<AppError, List<Patient>> getAllByMedication(String medicationName);

    Either<AppError, Integer> delete(int patientId, boolean confirmed);
}
