package dao;

import io.vavr.control.Either;
import model.PrescribedMedication;
import model.error.AppError;

import java.util.List;

public interface DaoPrescribedMedication {

    //SPRING
    Either<AppError, List<PrescribedMedication>> getAll();

    //save new medication to the newest medical record of a specific patient
    //SPRING
    Either<AppError,Integer> save(PrescribedMedication prescribedMedication);

    //save new medication to the newest medical record of a specific patient
    //SPRING
    Either<AppError, Integer> update(PrescribedMedication prescribedMedication);
}
