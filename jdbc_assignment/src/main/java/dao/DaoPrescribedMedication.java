package dao;

import io.vavr.control.Either;
import model.PrescribedMedication;
import model.error.AppError;

import java.util.ArrayList;
import java.util.List;

public interface DaoPrescribedMedication {

    Either<AppError, List<PrescribedMedication>> getAll(PrescribedMedication prescribedMedication);
}
