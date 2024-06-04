package dao.mongo.impl;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import common.Constants;
import common.config.Config;
import dao.mongo.DaoPatientMongo;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import model.error.PatientHasMedicalRecordsException;
import model.mongo.Patient;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.fields;

public class DaoPatientMongoImpl implements DaoPatientMongo {

    //We pass a gson instance with all the adapters (LocalDate, LocalDateTime, ObjectId)
    private final Gson gson;
    private final Config config;

    @Inject
    public DaoPatientMongoImpl(Gson gson, Config config) {
        this.gson = gson;
        this.config = config;
    }

    //getAll

    @Override
    public Either<AppError, List<Patient>> getAll() {
        Either<AppError, List<Patient>> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));
            List<Patient> patientList = new ArrayList<>();

            //we will exclude medical records for now
            List<Document> documents = patientCollection.find().projection(fields(exclude(Constants.MEDICAL_RECORDS))).into(new ArrayList<>());

            for (Document document : documents) {
                Patient patient = gson.fromJson(document.toJson(), Patient.class);
                patientList.add(patient);
            }
            result = Either.right(patientList);
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        }
        return result;
    }

    //get

    @Override
    public Either<AppError, Patient> get(ObjectId id) {
        Either<AppError, Patient> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));

            Document findResult = patientCollection.find(eq(Constants.OBJECT_ID, id)).first();

            if (findResult != null) {
                //we convert the Document to a Customer object
                Patient patient = gson.fromJson(findResult.toJson(), Patient.class);
                result = Either.right(patient);

            } else {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));

            }
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        }
        return result;
    }

    //save

    @Override
    public Either<AppError, Integer> save(Patient patient) {
        Either<AppError, Integer> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));

            //we create a new patient object
            Patient newPatient = patientConverter(patient);

            //we convert the patient to a JSON string
            String jsonPatient = gson.toJson(newPatient);

            //we convert the JSON string to a Document
            Document patientDocument = Document.parse(jsonPatient);

            //we insert the patient
            patientCollection.insertOne(patientDocument);

            result = Either.right(1);

        } catch (Exception e) {
            result = Either.left(new AppError(Constants.PATIENT_INSERTION_ERROR + e.getMessage()));
        }
        return result;
    }

    //update

    @Override
    public Either<AppError, Integer> update(Patient patient) {
        Either<AppError, Integer> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));

            //we convert the patient to a JSON string
            String jsonPatient = gson.toJson(patient);

            //we convert the JSON string to a Document
            Document patientDocument = Document.parse(jsonPatient);

            //we create a filter to find the patient by its id
            Document filter = new Document(Constants.OBJECT_ID, patient.getPatientId());

            //we update the patient
            UpdateResult updateResult = patientCollection.replaceOne(filter, patientDocument);

            if (updateResult.getModifiedCount() == 1) {
                result = Either.right(1);
            } else {
                result = Either.left(new AppError(Constants.PATIENT_UPDATE_ERROR));
            }

        } catch (Exception e) {
            result = Either.left(new AppError(Constants.PATIENT_UPDATE_ERROR + e.getMessage()));
        }
        return result;
    }

    //delete

    @Override
    public Either<AppError, Integer> delete(ObjectId patientId, Boolean confirmation) {
        Either<AppError, Integer> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));

            Document findResult = patientCollection.find(eq(Constants.OBJECT_ID, patientId)).first();

            if (findResult != null) {
                //we convert the Document to a Customer object
                Patient patient = gson.fromJson(findResult.toJson(), Patient.class);

                if (!confirmation) {
                    if (!patient.getMedicalRecords().isEmpty()) {
                        throw new PatientHasMedicalRecordsException(Constants.PATIENT_STILL_HAS_MEDICAL_RECORDS_ERROR);
                    }
                }
            } else {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            }
            patientCollection.deleteOne(eq(Constants.OBJECT_ID, patientId));
            result = Either.right(1);

        } catch (Exception e) {
            if (e instanceof PatientHasMedicalRecordsException) {
                result = Either.left(new AppError(e.getMessage()));
            } else {
                result = Either.left(new AppError(Constants.PATIENT_DELETION_ERROR + e.getMessage()));
            }

        }
        return result;
    }

    private Patient patientConverter(Patient patient) {

        if (patient.getMedicalRecords() == null) {
            patient.setMedicalRecords(new ArrayList<>());
        }

        return Patient.builder()
                //we don't pass the id because it's generated by mongo
                .name(patient.getName())
                .phone(patient.getPhone())
                .medicalRecords(patient.getMedicalRecords())
                .build();
    }
}
