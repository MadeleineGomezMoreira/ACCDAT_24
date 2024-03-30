package dao.txt;

import io.vavr.control.Either;
import model.PrescribedMedication;
import model.error.AppError;

import java.util.List;

public interface DaoPrescribedMedications {
    Either<AppError, List<PrescribedMedication>> getAll(PrescribedMedication medication);

    Either<AppError, List<PrescribedMedication>> getAll();

    Either<AppError, Integer> save(PrescribedMedication medication);

    //delete medication by medicalRecordId
    Either<AppError, Integer> delete(PrescribedMedication medication);
}
