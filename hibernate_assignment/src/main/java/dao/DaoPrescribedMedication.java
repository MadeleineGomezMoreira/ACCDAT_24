package dao;

import io.vavr.control.Either;
import model.PrescribedMedication;
import model.error.AppError;

public interface DaoPrescribedMedication {
    Either<AppError, Integer> save(PrescribedMedication prescribedMedication);
}
