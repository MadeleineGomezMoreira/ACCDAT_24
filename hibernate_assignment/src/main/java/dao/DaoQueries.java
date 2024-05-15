package dao;

import io.vavr.control.Either;
import model.dto.PatientWithMedNumber;
import model.error.AppError;

import java.time.LocalDate;

public interface DaoQueries {
    //GET THE PATIENT ID WITH THE MOST MEDICAL RECORDS
    Either<AppError, Integer> getQueryOne();

    //GET THE DATE WHEN MOST PATIENTS WERE ADMITTED
    Either<AppError, LocalDate> getQueryTwo();

    //GET THE NAME AND THE NUMBER OF MEDICATIONS OF EACH PATIENT
    Either<AppError, PatientWithMedNumber> getQueryThree();
}
