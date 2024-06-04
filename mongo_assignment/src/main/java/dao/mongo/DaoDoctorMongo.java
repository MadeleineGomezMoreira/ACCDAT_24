package dao.mongo;

import io.vavr.control.Either;
import model.error.AppError;
import model.mongo.Doctor;
import org.bson.types.ObjectId;

import java.util.List;

public interface DaoDoctorMongo {
    Either<AppError, Doctor> get(ObjectId id);

    //get all doctors that treated a specific patient
    Either<AppError, List<Doctor>> getAll(ObjectId objectId);

    //get all doctor ObjectIDs
    Either<AppError, List<ObjectId>> getAll();

    Either<AppError, Integer> save(Doctor doctor);
}
