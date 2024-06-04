package services;

import io.vavr.control.Either;
import model.error.AppError;

public interface DataMigrationService {
    Either<AppError, Integer> importDataToMongo();
}
