package services;

import io.vavr.control.Either;
import model.dto.PatientWithMedNumber;
import model.error.AppError;

import java.time.LocalDate;
import java.util.List;

public interface QueryService {
    Either<AppError, Integer> getPatientIdWithMostMedicalRecords();

    Either<AppError, LocalDate> getDateWhenMostPatientsWereAdmitted();

    Either<AppError, List<PatientWithMedNumber>> getNameAndNumberOfMedicationsOfEachPatient();
}
