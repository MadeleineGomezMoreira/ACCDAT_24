package dao.mongo;

import io.vavr.control.Either;
import model.error.AppError;
import model.mongo.Patient;
import org.bson.types.ObjectId;

import java.util.List;

public interface DaoPatientMongo {
    Either<AppError, List<Patient>> getAll();

    Either<AppError, Patient> get(ObjectId id);

    Either<AppError, Integer> save(Patient patient);

    Either<AppError, Integer> update(Patient patient);

    Either<AppError, Integer> delete(ObjectId patientId, Boolean confirmation);
}
