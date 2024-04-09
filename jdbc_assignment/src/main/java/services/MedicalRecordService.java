package services;

import io.vavr.control.Either;
import model.MedicalRecord;
import model.error.AppError;

import java.util.List;

public interface MedicalRecordService {
    Either<AppError, List<MedicalRecord>> getAllRecordsByPatient(int patientId);

    Either<AppError, Integer> saveMedicalRecordWithMedication(MedicalRecord medicalRecord);
}
