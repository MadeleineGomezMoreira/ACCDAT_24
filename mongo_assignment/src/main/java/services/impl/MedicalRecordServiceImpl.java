package services.impl;

import dao.DaoMedicalRecord;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MedicalRecord;
import model.dto.MedicalRecordDTO;
import model.dto.PatientWithRecordsDTO;
import model.error.AppError;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MedicalRecordServiceImpl implements services.MedicalRecordService {

    private final DaoMedicalRecord daoMedicalRecord;

    @Inject
    public MedicalRecordServiceImpl(DaoMedicalRecord daoMedicalRecord) {
        this.daoMedicalRecord = daoMedicalRecord;
    }

    @Override
    public Either<AppError, List<PatientWithRecordsDTO>> getRecordsByPatient() {
        Either<AppError, List<PatientWithRecordsDTO>> result;

        Either<AppError, List<MedicalRecord>> records = daoMedicalRecord.getAll();

        if (records.isLeft()) {
            result = Either.left(records.getLeft());
        } else {

            List<MedicalRecord> medicalRecords = records.get();
            List<PatientWithRecordsDTO> patientsWithRecordsList = new ArrayList<>();

            //we have to order the medical records by patientId and put them into the PatientWithRecordsDTO
            medicalRecords.stream()
                    .sorted(Comparator.comparingInt(MedicalRecord::getPatientId))
                    .forEach(medicalRecord -> {
                        PatientWithRecordsDTO patientWithRecords = new PatientWithRecordsDTO();
                        patientWithRecords.setPatientId(medicalRecord.getPatientId());
                        List<MedicalRecordDTO> medRecords = patientWithRecords.getMedicalRecords();
                        medRecords.add(new MedicalRecordDTO(medicalRecord.getId(), medicalRecord.getAdmissionDate(), medicalRecord.getDiagnosis(), medicalRecord.getDoctorId()));
                        patientWithRecords.setMedicalRecords(medRecords);
                        patientsWithRecordsList.add(patientWithRecords);
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
        return daoMedicalRecord.save(medicalRecord);
    }
}
