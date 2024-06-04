package dao.mongo.impl;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import common.Constants;
import common.config.Config;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import model.mongo.MedicalRecord;
import model.mongo.Patient;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DaoMedicalRecordMongoImpl implements dao.mongo.DaoMedicalRecordMongo {

    private final Gson gson;
    private final Config config;

    @Inject
    public DaoMedicalRecordMongoImpl(Gson gson, Config config) {
        this.gson = gson;
        this.config = config;
    }

    @Override
    //get all medical records (ordered by patientId)
    public Either<AppError, HashMap<ObjectId, List<MedicalRecord>>> getAll(){
        Either<AppError, HashMap<ObjectId, List<MedicalRecord>>> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));

            List<Document> patients = patientCollection.find().into(new ArrayList<>());

            HashMap<ObjectId, List<MedicalRecord>> medicalRecords = new HashMap<>();

            patients.forEach(patient -> {
                Patient patientItem = gson.fromJson(patient.toJson(), Patient.class);
                if(patientItem.getMedicalRecords() == null){
                    patientItem.setMedicalRecords(new ArrayList<>());
                }
                medicalRecords.put(patientItem.getPatientId(), patientItem.getMedicalRecords());
            });

            result = Either.right(medicalRecords);
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        }

        return result;
    }

    //get all medical records from a specific patient
    @Override
    public Either<AppError, List<MedicalRecord>> getAll(ObjectId patientId) {
        Either<AppError, List<MedicalRecord>> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));

            Document findResult = patientCollection.find(eq(Constants.OBJECT_ID, patientId)).first();

            if (findResult != null) {
                //we convert the Document to a Patient object
                Patient patient = gson.fromJson(findResult.toJson(), Patient.class);

                //now we get the patient's medical records
                List<MedicalRecord> medicalRecords = patient.getMedicalRecords();

                result = Either.right(medicalRecords);
            } else {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            }
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        }

        return result;
    }

    //save a medical record with medication
    @Override
    public Either<AppError, Integer> save(MedicalRecord medicalRecord) {
        Either<AppError, Integer> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));

            Document findResult = patientCollection.find(eq(Constants.OBJECT_ID, medicalRecord.getPatientId())).first();

            if (findResult != null) {
                //we convert the Document to a Patient object
                Patient patient = gson.fromJson(findResult.toJson(), Patient.class);

                //we add the medical record to the patient
                if(patient.getMedicalRecords() == null){
                    patient.setMedicalRecords(new ArrayList<>());
                }
                patient.getMedicalRecords().add(medicalRecord);

                //we make sure the patient object loses the patientId field (we do not want to insert that)
                patient = patientConverter(patient);

                //we update the patient
                UpdateResult updateResult= patientCollection.updateOne(eq(Constants.OBJECT_ID, medicalRecord.getPatientId()), new Document("$set", Document.parse(gson.toJson(patient))));

                if (updateResult.getModifiedCount() == 1) {
                    result = Either.right(1);
                } else {
                    result = Either.left(new AppError(Constants.MEDICAL_RECORD_INSERTION_ERROR));
                }
            } else {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            }
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        }

        return result;
    }

    //update medical record (adding new prescription)
    @Override
    public Either<AppError, Integer> update(MedicalRecord medicalRecord) {
        Either<AppError, Integer> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));

            Document findResult = patientCollection.find(eq(Constants.NAME, medicalRecord.getPatientName())).first();

            if (findResult != null) {
                //we convert the Document to a Patient object
                Patient patient = gson.fromJson(findResult.toJson(), Patient.class);

                //we get the medical record
                if(patient.getMedicalRecords() == null){
                    patient.setMedicalRecords(new ArrayList<>());
                }
                MedicalRecord record = patient.getMedicalRecords().stream().filter(medicalRecord1 -> medicalRecord1.getDiagnosis().equals(medicalRecord.getDiagnosis())).findFirst().orElse(null);

                if (record != null) {
                    //we add the new prescription
                    record.getPrescribedMedication().addAll(medicalRecord.getPrescribedMedication());

                    //we update the patient
                    patientCollection.updateOne(eq(Constants.NAME, medicalRecord.getPatientName()), new Document("$set", Document.parse(gson.toJson(patient))));

                    result = Either.right(1);
                } else {
                    result = Either.left(new AppError(Constants.PATIENT_HAS_NO_MEDICAL_RECORDS_ERROR));
                }
            } else {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            }
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        }

        return result;
    }

    //update medical record (removing a specific medication only if more than one exists)
    @Override
    public Either<AppError, Integer> update(MedicalRecord medicalRecord, String medicationToRemove) {
        Either<AppError, Integer> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));

            Document findResult = patientCollection.find(eq(Constants.NAME, medicalRecord.getPatientName())).first();

            if (findResult != null) {
                //convert the Document to a Patient object
                Patient patient = gson.fromJson(findResult.toJson(), Patient.class);

                //retrieve the medical record by its diagnosis
                MedicalRecord record = patient.getMedicalRecords().stream()
                        .filter(medRecord -> medRecord.getDiagnosis().equals(medicalRecord.getDiagnosis()))
                        .findFirst()
                        .orElse(null);

                if (record != null) {
                    //check if there are more than one prescribed medications
                    if (record.getPrescribedMedication().size() > 1) {
                        //remove the prescribed medication by name
                        record.getPrescribedMedication().removeIf(medication -> medication.getName().equals(medicationToRemove));

                        //update the patient document
                        patientCollection.updateOne(eq(Constants.NAME, medicalRecord.getPatientName()), new Document("$set", Document.parse(gson.toJson(patient))));

                        result = Either.right(1);
                    } else {
                        result = Either.left(new AppError(Constants.CANNOT_DELETE_MEDICATION_EMPTY_MEDICAL_RECORD));
                    }
                } else {
                    result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
                }
            } else {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            }
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
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
