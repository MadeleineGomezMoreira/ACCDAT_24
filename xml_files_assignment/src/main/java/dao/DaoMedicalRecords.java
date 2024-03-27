package dao;

import io.vavr.control.Either;
import model.MedicalRecord;
import model.error.AppError;

import java.util.List;

public interface DaoMedicalRecords {
    //get all medical records by patient id
    Either<AppError, List<MedicalRecord>> getAll(MedicalRecord record);

    Either<AppError, List<MedicalRecord>> getAll();

    Either<AppError, Integer> save(MedicalRecord record);

    //delete all medical records by patientId
    Either<AppError, Integer> delete(MedicalRecord record);
}
