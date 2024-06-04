package dao.mongo;

import io.vavr.control.Either;
import model.error.AppError;
import model.mongo.PrescribedMedication;
import org.bson.types.ObjectId;

import java.util.List;

public interface DaoPrescribedMedicationMongo {
    Either<AppError, List<PrescribedMedication>> getAll(ObjectId objectId);
}
