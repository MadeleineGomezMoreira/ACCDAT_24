package dao;

import io.vavr.control.Either;
import model.PrescribedMedication;
import model.error.AppError;

import java.util.List;

public interface DaoPrescribedMedication {
    Either<AppError, List<PrescribedMedication>> getAll(PrescribedMedication medication);

    Either<AppError, Integer> save(PrescribedMedication medication);

    //delete medication by medicalRecordId
    Either<AppError, Integer> delete(PrescribedMedication medication);
}
