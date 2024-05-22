package dao;

import io.vavr.control.Either;
import model.MedicalRecord;
import model.error.AppError;

import java.util.List;

public interface DaoMedicalRecord {
    Either<AppError, List<MedicalRecord>> getAll();

    //get all medical records by patient
    Either<AppError, List<MedicalRecord>> getAll(MedicalRecord medicalRecord);

    Either<AppError, MedicalRecord> get(MedicalRecord medicalRecord);

    Either<AppError, Integer> save(MedicalRecord medicalRecord);

    //delete all medical records older than 2024
    Either<AppError, Integer> delete();
}
