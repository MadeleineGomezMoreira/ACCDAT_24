package services;

import io.vavr.control.Either;
import model.MedicalRecord;
import model.error.AppError;

import java.util.List;

public interface MedicalRecordService {
    Either<AppError, List<MedicalRecord>> showRecordsWithMedications();

    Either<AppError, Integer> deleteOldAndSaveXML();
}
