package services;

import io.vavr.control.Either;
import model.error.AppError;

import java.time.LocalDate;

public interface AppointmentService {
    Either<AppError, LocalDate> getMostSharedDate();
}
