package services;

import io.vavr.control.Either;
import model.dto.PatientWithRecordsDTO;
import model.error.AppError;

import java.util.List;

public interface MedicalRecordService {
    Either<AppError, List<PatientWithRecordsDTO>> getRecordsByPatient();

    Either<AppError, Integer> deleteOlderThan2024();
}
