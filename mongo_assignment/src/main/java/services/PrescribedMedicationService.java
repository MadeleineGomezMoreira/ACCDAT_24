package services;

import io.vavr.control.Either;
import model.PrescribedMedication;
import model.dto.RecordWithPrescriptionsDTO;
import model.error.AppError;

import java.util.List;

public interface PrescribedMedicationService {
    Either<AppError, Integer> save(PrescribedMedication prescribedMedication);

    Either<AppError, List<RecordWithPrescriptionsDTO>> getRecordsWithPrescription();
}
