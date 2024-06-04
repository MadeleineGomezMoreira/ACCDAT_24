package dao.mongo;

import io.vavr.control.Either;
import model.error.AppError;
import model.mongo.MedicalRecord;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;

public interface DaoMedicalRecordMongo {
    //get all medical records (ordered by patientId)
    Either<AppError, HashMap<ObjectId, List<MedicalRecord>>> getAll();

    //get all medical records from a specific patient
    Either<AppError, List<MedicalRecord>> getAll(ObjectId patientId);

    //save a medical record with medication
    Either<AppError, Integer> save(MedicalRecord medicalRecord);

    //update medical record (adding new prescription)
    Either<AppError, Integer> update(MedicalRecord medicalRecord);

    //update medical record (removing a specific medication only if more than one exists)
    Either<AppError, Integer> update(MedicalRecord medicalRecord, String medicationToRemove);
}
