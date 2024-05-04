package dao;

import io.vavr.control.Either;
import model.MedicalRecord;
import model.error.AppError;

import java.util.List;

public interface DaoMedicalRecord {

    Either<AppError, List<MedicalRecord>> getAll();

    Either<AppError, MedicalRecord> get(MedicalRecord medicalRecord);

    Either<AppError, Integer> delete();

}
