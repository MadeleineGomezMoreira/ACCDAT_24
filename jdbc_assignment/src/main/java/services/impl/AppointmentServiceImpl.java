package services.impl;

import dao.DaoAppointment;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;

import java.time.LocalDate;

public class AppointmentServiceImpl implements services.AppointmentService {
    private final DaoAppointment daoAppointment;

    @Inject
    public AppointmentServiceImpl(DaoAppointment daoAppointment) {
        this.daoAppointment = daoAppointment;
    }

    @Override
    public Either<AppError, LocalDate> getMostSharedDate() {
        Either<AppError, LocalDate> result;
        if (daoAppointment.get().isRight()) {
            result = Either.right(daoAppointment.get().get().getAppointmentDate());
        } else {
            result = Either.left(daoAppointment.get().getLeft());
        }
        return result;
    }
}
