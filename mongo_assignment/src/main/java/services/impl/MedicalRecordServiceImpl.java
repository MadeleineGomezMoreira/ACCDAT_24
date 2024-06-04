package services.impl;

import dao.hibernate.DaoMedicalRecord;
import dao.mongo.DaoMedicalRecordMongo;
import io.vavr.control.Either;
import jakarta.inject.Inject;;
import model.dto.MedicalRecordDTO;
import model.dto.PatientWithRecordsDTO;
import model.error.AppError;
import model.mongo.MedicalRecord;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicalRecordServiceImpl implements services.MedicalRecordService {

    private final DaoMedicalRecord daoMedicalRecord;
    private final DaoMedicalRecordMongo daoMedicalRecordMongo;

    @Inject
    public MedicalRecordServiceImpl(DaoMedicalRecord daoMedicalRecord, DaoMedicalRecordMongo daoMedicalRecordMongo) {
        this.daoMedicalRecord = daoMedicalRecord;
        this.daoMedicalRecordMongo = daoMedicalRecordMongo;
    }

    @Override
    public Either<AppError, List<PatientWithRecordsDTO>> getAllRecordsByPatient() {
        Either<AppError, List<PatientWithRecordsDTO>> result;

        Either<AppError, HashMap<ObjectId, List<MedicalRecord>>> recordsByPatient = daoMedicalRecordMongo.getAll();

        if (recordsByPatient.isLeft()) {
            result = Either.left(recordsByPatient.getLeft());
        } else {

            HashMap<ObjectId, List<MedicalRecord>> medicalRecordEntities = recordsByPatient.get();
            List<PatientWithRecordsDTO> patientsWithRecordsList = new ArrayList<>();

            medicalRecordEntities.forEach((key, value) -> {
                PatientWithRecordsDTO patientWithRecordsDTO = new PatientWithRecordsDTO();
                patientWithRecordsDTO.setPatientId(key);
                patientWithRecordsDTO.setMedicalRecords(convertToDTO(value));
                patientsWithRecordsList.add(patientWithRecordsDTO);
            });
            result = Either.right(patientsWithRecordsList);
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> deleteOlderThan2024() {
        return daoMedicalRecord.delete();
    }

    @Override
    public Either<AppError, Integer> save(MedicalRecord medicalRecord) {
        return daoMedicalRecordMongo.save(medicalRecord);
    }

    private List<MedicalRecordDTO> convertToDTO(List<MedicalRecord> medicalRecords) {
        List<MedicalRecordDTO> medicalRecordDTOs = new ArrayList<>();
        medicalRecords.forEach(medicalRecord -> {
            MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
            medicalRecordDTO.setDoctorId(medicalRecord.getDoctorId());
            medicalRecordDTO.setDiagnosis(medicalRecord.getDiagnosis());
            medicalRecordDTO.setAdmissionDate(medicalRecord.getAdmissionDate());
            medicalRecordDTOs.add(medicalRecordDTO);
        });
        return medicalRecordDTOs;
    }
}
