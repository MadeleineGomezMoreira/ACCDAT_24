package dao.mongo;

import io.vavr.control.Either;
import model.error.AppError;
import model.mongo.PrescribedMedication;
import org.bson.types.ObjectId;

import java.util.List;

public interface DaoPrescribedMedicationMongo {
    Either<AppError, List<PrescribedMedication>> getAll(ObjectId objectId);

    //save a new PrescribedMedication to the patient's last medical record
    Either<AppError, Integer> save(PrescribedMedication medication);

    //delete a prescribed medication from the patient's last medical record
    Either<AppError, Integer> delete(ObjectId patientId);
}
