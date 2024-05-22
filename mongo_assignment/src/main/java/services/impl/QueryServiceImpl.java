package services.impl;

import dao.DaoQueries;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.dto.PatientWithMedNumber;
import model.error.AppError;

import java.time.LocalDate;
import java.util.List;

public class QueryServiceImpl implements services.QueryService {
    private final DaoQueries daoQueries;

    @Inject
    public QueryServiceImpl(DaoQueries daoQueries) {
        this.daoQueries = daoQueries;
    }

    @Override
    public Either<AppError, Integer> getPatientIdWithMostMedicalRecords() {
        return daoQueries.getQueryOne();
    }

    @Override
    public Either<AppError, LocalDate> getDateWhenMostPatientsWereAdmitted() {
        return daoQueries.getQueryTwo();
    }

    @Override
    public Either<AppError, List<PatientWithMedNumber>> getNameAndNumberOfMedicationsOfEachPatient() {
        return daoQueries.getQueryThree();
    }
}
