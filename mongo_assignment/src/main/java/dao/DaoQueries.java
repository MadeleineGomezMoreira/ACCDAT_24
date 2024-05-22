package dao;

import io.vavr.control.Either;
import model.dto.PatientWithMedNumber;
import model.error.AppError;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DaoQueries {
    //GET THE PATIENT ID WITH THE MOST MEDICAL RECORDS
    Either<AppError, Integer> getQueryOne();

    //GET THE DATE WHEN MOST PATIENTS WERE ADMITTED
    Either<AppError, LocalDate> getQueryTwo();

    //GET THE NAME AND THE NUMBER OF MEDICATIONS OF EACH PATIENT
    Either<AppError, List<PatientWithMedNumber>> getQueryThree();

    //GET THE TOTAL AMOUNT PAID BY EACH PATIENT
    Either<AppError, Map<Integer, Double>> getQueryFour();
}
