package services.impl;

import dao.DaoMedicalRecord;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MedicalRecord;
import model.error.AppError;

import java.util.List;

public class MedicalRecordServiceImpl implements services.MedicalRecordService {

    private final DaoMedicalRecord daoMedicalRecord;

    @Inject
    public MedicalRecordServiceImpl(DaoMedicalRecord daoMedicalRecord) {
        this.daoMedicalRecord = daoMedicalRecord;
    }

    @Override
    public Either<AppError, List<MedicalRecord>> getAllRecordsByPatient(int patientId) {
        return daoMedicalRecord.getAll(new MedicalRecord(patientId));
    }

    @Override
    public Either<AppError, Integer> saveMedicalRecordWithMedication(MedicalRecord medicalRecord) {
        return daoMedicalRecord.save(medicalRecord);
    }
}
