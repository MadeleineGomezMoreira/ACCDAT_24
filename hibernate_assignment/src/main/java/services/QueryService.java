package services;

import io.vavr.control.Either;
import model.dto.PatientWithMedNumber;
import model.error.AppError;

import java.time.LocalDate;

public interface QueryService {
    Either<AppError, Integer> getPatientIdWithMostMedicalRecords();

    Either<AppError, LocalDate> getDateWhenMostPatientsWereAdmitted();

    Either<AppError, PatientWithMedNumber> getNameAndNumberOfMedicationsOfEachPatient();
}
