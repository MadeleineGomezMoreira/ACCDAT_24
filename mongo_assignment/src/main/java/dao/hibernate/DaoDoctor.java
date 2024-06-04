package dao.hibernate;

import io.vavr.control.Either;
import model.hibernate.DoctorEntity;
import model.error.AppError;

import java.util.List;

public interface DaoDoctor {


    //get all
    Either<AppError, List<DoctorEntity>> getAll();
}
