package dao;

import io.vavr.control.Either;
import model.dto.PatientMedicationAmountDTO;
import model.error.AppError;

import java.util.List;

public interface DaoQueries {
    Either<AppError, List<String>> getQueryOne();

    Either<AppError, List<PatientMedicationAmountDTO>> getQueryTwo();
}
