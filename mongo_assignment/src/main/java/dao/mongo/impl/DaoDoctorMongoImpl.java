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
import model.mongo.Doctor;
import model.mongo.MedicalRecord;
import model.mongo.Patient;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;

public class DaoDoctorMongoImpl implements dao.mongo.DaoDoctorMongo {

    private final Gson gson;
    private final Config config;

    @Inject
    public DaoDoctorMongoImpl(Gson gson, Config config) {
        this.gson = gson;
        this.config = config;
    }

    //get
    @Override
    public Either<AppError, Doctor> get(ObjectId id) {
        Either<AppError, Doctor> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> doctorCollection = db.getCollection(config.getProperty(Constants.DOCTORS_COLLECTION));

            Document findResult = doctorCollection.find(eq(Constants.OBJECT_ID, id)).first();

            if (findResult == null) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                //we will convert the Document to a Doctor object
                Doctor doctor = gson.fromJson(findResult.toJson(), Doctor.class);
                result = Either.right(doctor);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        }
        return result;
    }

    //get all doctors that treated a specific patient
    @Override
    public Either<AppError, List<Doctor>> getAll(ObjectId objectId) {
        Either<AppError, List<Doctor>> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));
            MongoCollection<Document> doctorCollection = db.getCollection(config.getProperty(Constants.DOCTORS_COLLECTION));

            Document findResult = patientCollection.find(eq(Constants.OBJECT_ID, objectId)).first();

            if (findResult != null) {
                //we convert the Document to a Patient object
                Patient patient = gson.fromJson(findResult.toJson(), Patient.class);

                //set medical records to empty list if null
                if (patient.getMedicalRecords() == null) {
                    patient.setMedicalRecords(new ArrayList<>());
                }

                //extract the doctor ObjectIds from the patient's medical records
                Set<ObjectId> doctorIdList = new HashSet<>();
                for (MedicalRecord record : patient.getMedicalRecords()) {
                    doctorIdList.add(record.getDoctorId());
                }

                //get the doctors from mongo collection
                List<Doctor> doctors = new ArrayList<>();
                for (ObjectId doctorId : doctorIdList) {
                    Document doctorDoc = doctorCollection.find(eq(Constants.OBJECT_ID, doctorId)).first();
                    if (doctorDoc != null) {
                        Doctor doc = gson.fromJson(doctorDoc.toJson(), Doctor.class);
                        doctors.add(doc);
                    }
                }

                if (doctors.isEmpty()) {
                    result = Either.left(new AppError(Constants.NO_DOCTORS_TREATED_THIS_PATIENT_ERROR));
                } else {
                    result = Either.right(doctors);
                }

            } else {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            }
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        }
        return result;
    }

    @Override
    //get all doctor ObjectIDs
    public Either<AppError, List<ObjectId>> getAll() {
        Either<AppError, List<ObjectId>> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> doctorCollection = db.getCollection(config.getProperty(Constants.DOCTORS_COLLECTION));

            // Fetch only the ObjectIds
            List<ObjectId> doctorIds = doctorCollection.find().projection(include("_id")).map(doc -> doc.getObjectId("_id")).into(new ArrayList<>());

            if (doctorIds.isEmpty()) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                result = Either.right(doctorIds);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        }
        return result;
    }

    //save

    @Override
    public Either<AppError, Integer> save(Doctor doctor) {
        Either<AppError, Integer> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> doctorCollection = db.getCollection(config.getProperty(Constants.DOCTORS_COLLECTION));

            //we create a new patient object
            Doctor newDoctor = doctorConverter(doctor);

            //we convert the patient to a JSON string
            String jsonDoctor = gson.toJson(newDoctor);

            //we convert the JSON string to a Document
            Document doctorDocument = Document.parse(jsonDoctor);

            //we insert the patient
            doctorCollection.insertOne(doctorDocument);

            result = Either.right(1);

        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DOCTOR_INSERTION_ERROR + e.getMessage()));
        }
        return result;
    }

    private Doctor doctorConverter(Doctor doctor) {

        return Doctor.builder()
                //we don't pass the id because it's generated by mongo
                .name(doctor.getName())
                .specialty(doctor.getSpecialty())
                .phone(doctor.getPhone())
                .build();
    }
}
