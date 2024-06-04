package services.impl;

import dao.mongo.DaoDoctorMongo;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import model.mongo.Doctor;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;

public class DoctorServiceImpl implements services.DoctorService {

    private final DaoDoctorMongo daoDoctorMongo;

    @Inject
    public DoctorServiceImpl(DaoDoctorMongo daoDoctorMongo) {
        this.daoDoctorMongo = daoDoctorMongo;
    }

    @Override
    public Either<AppError, List<String>> getDoctorsWhoTreatedOnePatient(ObjectId objectId) {

        Either<AppError, List<Doctor>> doctors = daoDoctorMongo.getAll(objectId);

        if (doctors.isRight()) {
            List<String> doctorsNames = doctors.get().stream().map(Doctor::getName).toList();
            return Either.right(doctorsNames);
        } else {
            return Either.left(doctors.getLeft());
        }
    }

    @Override
    public Either<AppError, HashMap<Integer, ObjectId>> getAllDoctorsIDs() {
        Either<AppError, List<ObjectId>> doctorIds = daoDoctorMongo.getAll();

        if (doctorIds.isRight()) {
            List<ObjectId> ids = doctorIds.get();
            HashMap<Integer, ObjectId> doctorIdsMap = new HashMap<>();

            for (int i = 0; i < ids.size(); i++) {
                doctorIdsMap.put(i + 1, ids.get(i));
            }

            return Either.right(doctorIdsMap);
        } else {
            return Either.left(doctorIds.getLeft());
        }
    }
}
