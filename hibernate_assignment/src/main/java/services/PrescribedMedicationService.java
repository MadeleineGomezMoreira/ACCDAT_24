package services;

import io.vavr.control.Either;
import model.PrescribedMedication;
import model.error.AppError;

public interface PrescribedMedicationService {
    Either<AppError, Integer> save(PrescribedMedication prescribedMedication);
}
