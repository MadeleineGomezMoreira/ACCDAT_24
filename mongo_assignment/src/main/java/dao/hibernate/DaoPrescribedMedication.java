package dao.hibernate;

import io.vavr.control.Either;
import model.hibernate.PrescribedMedicationEntity;
import model.error.AppError;

import java.util.List;

public interface DaoPrescribedMedication {
    Either<AppError, List<PrescribedMedicationEntity>> getAll();

    Either<AppError, Integer> save(PrescribedMedicationEntity prescribedMedicationEntity);
}
