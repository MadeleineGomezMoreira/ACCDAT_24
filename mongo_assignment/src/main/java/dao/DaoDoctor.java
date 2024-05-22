package dao;

import io.vavr.control.Either;
import model.Doctor;
import model.error.AppError;

public interface DaoDoctor {
    Either<AppError, Doctor> get(Doctor doctor);
}
