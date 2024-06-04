package dao.mongo.impl;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import common.Constants;
import common.config.Config;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import model.mongo.MedicalRecord;
import model.mongo.Patient;
import model.mongo.PrescribedMedication;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DaoPrescribedMedicationMongoImpl implements dao.mongo.DaoPrescribedMedicationMongo {

    private final Gson gson;
    private final Config config;

    @Inject
    public DaoPrescribedMedicationMongoImpl(Gson gson, Config config) {
        this.gson = gson;
        this.config = config;
    }

    @Override
    public Either<AppError, List<PrescribedMedication>> getAll(ObjectId objectId) {
        Either<AppError, List<PrescribedMedication>> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));

            Document findResult = patientCollection.find(eq(Constants.OBJECT_ID, objectId)).first();

            if (findResult != null) {
                //we convert the Document to a Patient object
                Patient patient = gson.fromJson(findResult.toJson(), Patient.class);

                //now we get the medication from the patient's medical records
                List<PrescribedMedication> prescribedMedications = new ArrayList<>();
                for (MedicalRecord record : patient.getMedicalRecords()) {
                    prescribedMedications.addAll(record.getPrescribedMedication());
                }

                result = Either.right(prescribedMedications);

            } else {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));

            }
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        }
        return result;
    }
}
