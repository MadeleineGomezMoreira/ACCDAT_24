package dao;

import io.vavr.control.Either;
import model.Appointment;
import model.error.AppError;

public interface DaoAppointment {
    //get the appointment date shared by the most patients
    Either<AppError, Appointment> get();
}
