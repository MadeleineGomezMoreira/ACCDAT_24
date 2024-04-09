package services;

import io.vavr.control.Either;
import model.PrescribedMedication;
import model.error.AppError;

import java.util.List;

public interface PrescribedMedicationService {
    Either<AppError, List<PrescribedMedication>> getPrescribedMedicationByPatient(int patientId);
}
