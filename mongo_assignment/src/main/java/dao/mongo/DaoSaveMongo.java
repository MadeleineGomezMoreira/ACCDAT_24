package dao.mongo;

import io.vavr.control.Either;
import model.error.AppError;
import model.mongo.Doctor;
import model.mongo.Patient;

import java.util.List;

public interface DaoSaveMongo {
    Either<AppError, Integer> saveAllPatients(List<Patient> patients);

    Either<AppError, Integer> saveAllDoctors(List<Doctor> doctors);
}
