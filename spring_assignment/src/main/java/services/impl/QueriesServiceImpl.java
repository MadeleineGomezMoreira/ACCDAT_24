package services.impl;

import dao.DaoQueries;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.dto.PatientMedicationAmountDTO;
import model.error.AppError;

import java.util.List;

public class QueriesServiceImpl implements services.QueriesService {

    private final DaoQueries daoQueries;

    @Inject
    public QueriesServiceImpl(DaoQueries daoQueries) {
        this.daoQueries = daoQueries;
    }

    @Override
    public Either<AppError, List<String>> getQueryOne() {
        return daoQueries.getQueryOne();
    }

    @Override
    public Either<AppError, List<PatientMedicationAmountDTO>> getQueryTwo() {
        return daoQueries.getQueryTwo();
    }


}
