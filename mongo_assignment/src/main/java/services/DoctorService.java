package services;

import io.vavr.control.Either;
import model.error.AppError;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;

public interface DoctorService {
    Either<AppError, List<String>> getDoctorsWhoTreatedOnePatient(ObjectId objectId);

    Either<AppError, HashMap<Integer, ObjectId>> getAllDoctorsIDs();
}
