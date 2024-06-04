package dao.hibernate;

import io.vavr.control.Either;
import model.hibernate.DoctorEntity;
import model.error.AppError;

public interface DaoDoctor {
    Either<AppError, DoctorEntity> get(DoctorEntity doctorEntity);
}
