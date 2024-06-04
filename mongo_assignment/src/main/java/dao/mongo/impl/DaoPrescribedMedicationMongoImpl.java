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

                if(patient.getMedicalRecords() == null){
                    patient.setMedicalRecords(new ArrayList<>());
                }

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

    //save a new PrescribedMedication to the patient's last medical record
    @Override
    public Either<AppError, Integer> save(PrescribedMedication medication) {
        Either<AppError, Integer> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));

            Document findResult = patientCollection.find(eq(Constants.OBJECT_ID, medication.getPatientId())).first();

            if (findResult != null) {
                //we convert the Document to a Patient object
                Patient patient = gson.fromJson(findResult.toJson(), Patient.class);

                //we get the last medical record
                MedicalRecord lastRecord = patient.getMedicalRecords().get(patient.getMedicalRecords().size() - 1);

                //we add the new medication to the last medical record
                lastRecord.getPrescribedMedication().add(medication);

                //we update the patient's medical records
                patient.getMedicalRecords().set(patient.getMedicalRecords().size() - 1, lastRecord);

                //we update the patient's document in the collection
                patientCollection.replaceOne(eq(Constants.OBJECT_ID, medication.getPatientId()), Document.parse(gson.toJson(patient)));

                result = Either.right(1);

            } else {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            }
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        }
        return result;
    }

    //delete the last prescribed medication from the patient's last medical record
    @Override
    public Either<AppError, Integer> delete(ObjectId patientId) {
        Either<AppError, Integer> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));

            Document findResult = patientCollection.find(eq(Constants.OBJECT_ID, patientId)).first();

            if (findResult != null) {
                //we convert the Document to a Patient object
                Patient patient = gson.fromJson(findResult.toJson(), Patient.class);

                //we get the last medical record
                MedicalRecord lastRecord = patient.getMedicalRecords().get(patient.getMedicalRecords().size() - 1);

                //we remove the last medication from the last medical record
                lastRecord.getPrescribedMedication().remove(lastRecord.getPrescribedMedication().size() - 1);

                //we update the patient's medical records
                patient.getMedicalRecords().set(patient.getMedicalRecords().size() - 1, lastRecord);

                //we update the patient's document in the collection
                patientCollection.replaceOne(eq(Constants.OBJECT_ID, patientId), Document.parse(gson.toJson(patient)));

                result = Either.right(1);

            } else {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            }
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        }
        return result;
    }
}
