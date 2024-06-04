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

import java.util.ArrayList;
import java.util.List;

public class DaoSaveMongoImpl implements dao.mongo.DaoSaveMongo {

    private final Gson gson;
    private final Config config;

    @Inject
    public DaoSaveMongoImpl(Gson gson, Config config) {
        this.gson = gson;
        this.config = config;
    }

    @Override
    public Either<AppError, Integer> saveAllPatients(List<Patient> patients) {
        Either<AppError, Integer> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> patientCollection = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));

            List<Patient> patientList = new ArrayList<>();
            patients.stream().map(this::patientConverter).forEach(patientList::add);

            patientCollection.insertMany(patientList.stream().map(patient -> Document.parse(gson.toJson(patient))).toList());

            result = Either.right(1);
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.PATIENT_INSERTION_ERROR + e.getMessage()));
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> saveAllDoctors(List<Doctor> doctors) {
        Either<AppError, Integer> result;

        try (MongoClient mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT))) {
            MongoDatabase db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));

            MongoCollection<Document> doctorCollection = db.getCollection(config.getProperty(Constants.DOCTORS_COLLECTION));

            List<Doctor> doctorList = new ArrayList<>();
            doctors.stream().map(this::doctorConverter).forEach(doctorList::add);

            doctorCollection.insertMany(doctorList.stream().map(
                    doctor -> Document.parse(gson.toJson(doctor)))
                    .toList());

            result = Either.right(1);
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DOCTOR_INSERTION_ERROR + e.getMessage()));
        }
        return result;
    }

    private Patient patientConverter(Patient patient) {

        if (patient.getMedicalRecords() == null) {
            patient.setMedicalRecords(new ArrayList<>());
        }

        return Patient.builder()
                //we will pass the id because we did generate it using hibernate
                .patientId(patient.getPatientId())
                .name(patient.getName())
                .phone(patient.getPhone())
                .medicalRecords(patient.getMedicalRecords().stream().map(
                        medicalRecord -> MedicalRecord.builder()
                                .doctorId(medicalRecord.getDoctorId())
                                .diagnosis(medicalRecord.getDiagnosis())
                                .admissionDate(medicalRecord.getAdmissionDate())
                                .prescribedMedication(medicalRecord.getPrescribedMedication())
                                .build()
                ).toList())
                .build();
    }

    private Doctor doctorConverter(Doctor doctor) {

        return Doctor.builder()
                ///we will pass the id because we did generate it using hibernate
                .doctorId(doctor.getDoctorId())
                .name(doctor.getName())
                .specialty(doctor.getSpecialty())
                .phone(doctor.getPhone())
                .build();
    }

}
