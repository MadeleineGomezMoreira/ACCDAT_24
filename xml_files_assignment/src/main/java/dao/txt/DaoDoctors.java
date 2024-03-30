package dao.txt;

import io.vavr.control.Either;
import model.Doctor;
import model.error.AppError;

public interface DaoDoctors {
    //get doctor by id
    Either<AppError, Doctor> get(Doctor doctor);
}
