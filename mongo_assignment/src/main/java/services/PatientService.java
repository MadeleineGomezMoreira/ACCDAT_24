package services;

import io.vavr.control.Either;
import model.error.AppError;
import model.hibernate.CredentialEntity;
import model.mongo.Patient;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;

public interface PatientService {
    Either<AppError, List<Patient>> getAllPatients();

    Either<AppError, HashMap<Integer, ObjectId>> getAllPatientsIDs();

    Either<AppError, Patient> getPatientById(ObjectId id);

    Either<AppError, Integer> updatePatient(Patient patient);

    Either<AppError, Integer> savePatient(Patient patient, CredentialEntity credentialEntity);

    Either<AppError, Integer> deletePatient(ObjectId patientId, Boolean confirmation);
}
