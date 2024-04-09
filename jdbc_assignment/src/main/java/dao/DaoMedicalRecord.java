package dao;

import io.vavr.control.Either;
import model.MedicalRecord;
import model.error.AppError;

import java.util.List;

public interface DaoMedicalRecord {
    Either<AppError, List<MedicalRecord>> getAll(MedicalRecord medicalRecord);

    Either<AppError, Integer> save(MedicalRecord medicalRecord);
}
