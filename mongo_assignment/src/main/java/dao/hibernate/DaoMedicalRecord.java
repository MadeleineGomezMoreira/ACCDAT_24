package dao.hibernate;

import io.vavr.control.Either;
import model.hibernate.MedicalRecordEntity;
import model.error.AppError;

import java.util.List;

public interface DaoMedicalRecord {
    Either<AppError, List<MedicalRecordEntity>> getAll();

    //get all medical records by patient
    Either<AppError, List<MedicalRecordEntity>> getAll(MedicalRecordEntity medicalRecordEntity);

    Either<AppError, MedicalRecordEntity> get(MedicalRecordEntity medicalRecordEntity);

    Either<AppError, Integer> save(MedicalRecordEntity medicalRecordEntity);

    //delete all medical records older than 2024
    Either<AppError, Integer> delete();
}
