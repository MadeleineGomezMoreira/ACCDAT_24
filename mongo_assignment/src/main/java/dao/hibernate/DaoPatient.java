package dao.hibernate;

import io.vavr.control.Either;
import model.hibernate.PatientEntity;
import model.error.AppError;

import java.util.List;

public interface DaoPatient {
    Either<AppError, List<PatientEntity>> getAll();

    Either<AppError, PatientEntity> get(PatientEntity patientEntity);

    Either<AppError, Integer> save(PatientEntity patientEntity);

    Either<AppError, Integer> update(PatientEntity patientEntity);

    Either<AppError, Integer> delete(PatientEntity patientEntity, Boolean confirmation);
}
