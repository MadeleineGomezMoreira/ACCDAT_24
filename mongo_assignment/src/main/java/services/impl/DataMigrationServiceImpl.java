package services.impl;

import common.Constants;
import dao.hibernate.DaoDoctor;
import dao.hibernate.DaoPatient;
import dao.mongo.DaoSaveMongo;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import model.hibernate.DoctorEntity;
import model.hibernate.MedicalRecordEntity;
import model.hibernate.PatientEntity;
import model.hibernate.PrescribedMedicationEntity;
import model.mongo.Doctor;
import model.mongo.MedicalRecord;
import model.mongo.Patient;
import model.mongo.PrescribedMedication;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataMigrationServiceImpl implements services.DataMigrationService {

    private final DaoSaveMongo daoSaveMongo;
    private final DaoPatient daoPatient;
    private final DaoDoctor daoDoctor;

    private final Map<Integer, ObjectId> patientIdMapping = new HashMap<>();
    private final Map<Integer, ObjectId> doctorIdMapping = new HashMap<>();

    @Inject
    public DataMigrationServiceImpl(DaoSaveMongo daoSaveMongo, DaoPatient daoPatient, DaoDoctor daoDoctor) {
        this.daoSaveMongo = daoSaveMongo;
        this.daoPatient = daoPatient;
        this.daoDoctor = daoDoctor;
    }

    @Override
    public Either<AppError, Integer> importDataToMongo() {
        Either<AppError, Integer> result;
        Either<AppError, List<PatientEntity>> patientList = daoPatient.getAll();
        Either<AppError, List<DoctorEntity>> doctorList = daoDoctor.getAll();

        if (patientList.isRight() && doctorList.isRight()) {
            List<Doctor> doctors = doctorEntityToDomain(doctorList.get());
            List<Patient> patients = patientEntityToDomain(patientList.get());

            daoSaveMongo.saveAllDoctors(doctors);
            result = daoSaveMongo.saveAllPatients(patients);
        } else {
            result = Either.left(new AppError(Constants.DATA_MIGRATION_ERROR));
        }
        return result;
    }

    private List<Doctor> doctorEntityToDomain(List<DoctorEntity> doctorList) {
        return doctorList.stream()
                .map(doctor -> {
                    ObjectId objectId = new ObjectId();
                    doctorIdMapping.put(doctor.getId(), objectId);
                    return Doctor.builder()
                            .doctorId(objectId)
                            .name(doctor.getName())
                            .phone(doctor.getPhone())
                            .specialty(doctor.getSpecialty())
                            .build();
                })
                .toList();
    }

    private List<PrescribedMedication> medicationEntityToDomain(List<PrescribedMedicationEntity> medicationList) {
        return medicationList.stream()
                .map(medication -> PrescribedMedication.builder()
                        .name(medication.getName())
                        .dose(medication.getDose())
                        .build())
                .toList();
    }

    private List<MedicalRecord> recordEntityToDomain(List<MedicalRecordEntity> recordList) {
        return recordList.stream()
                .map(record -> {
                    ObjectId doctorObjectId = doctorIdMapping.get(record.getDoctorId());
                    return MedicalRecord.builder()
                            .doctorId(doctorObjectId)
                            .diagnosis(record.getDiagnosis())
                            .admissionDate(record.getAdmissionDate())
                            .prescribedMedication(medicationEntityToDomain(record.getPrescribedMedication()))
                            .build();
                })
                .toList();
    }

    private List<Patient> patientEntityToDomain(List<PatientEntity> patientList) {
        return patientList.stream()
                .map(patient -> {
                    ObjectId objectId = new ObjectId();
                    patientIdMapping.put(patient.getId(), objectId);
                    return Patient.builder()
                            .patientId(objectId)
                            .name(patient.getName())
                            .birthDate(patient.getBirthDate())
                            .phone(patient.getPhone())
                            .medicalRecords(recordEntityToDomain(patient.getMedicalRecords()))
                            .build();
                })
                .toList();
    }


}
