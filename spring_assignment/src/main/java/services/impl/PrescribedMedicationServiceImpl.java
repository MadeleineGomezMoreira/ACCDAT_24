package services.impl;

import dao.DaoMedicalRecord;
import dao.DaoPrescribedMedication;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MedicalRecord;
import model.PrescribedMedication;
import model.error.AppError;

import java.util.List;

public class PrescribedMedicationServiceImpl implements services.PrescribedMedicationService {

    private final DaoPrescribedMedication daoPrescribedMedication;
    private final DaoMedicalRecord daoMedicalRecord;

    @Inject
    public PrescribedMedicationServiceImpl(DaoPrescribedMedication daoPrescribedMedication, DaoMedicalRecord daoMedicalRecord) {
        this.daoPrescribedMedication = daoPrescribedMedication;
        this.daoMedicalRecord = daoMedicalRecord;
    }

    @Override
    public Either<AppError, List<PrescribedMedication>> getAllPrescribedMedication() {
        return daoPrescribedMedication.getAll();
    }

    @Override
    public Either<AppError, Integer> updateMedicationDose(PrescribedMedication prescribedMedication) {
        return daoPrescribedMedication.update(prescribedMedication);
    }

    @Override
    public Either<AppError, Integer> saveMedicationToNewestRecord(PrescribedMedication prescribedMedication) {
        int patientId = prescribedMedication.getId();

        //first we need to get the newest record
        Either<AppError, MedicalRecord> getNewestRecord = daoMedicalRecord.get(new MedicalRecord(patientId));

        if (getNewestRecord.isRight()) {
            MedicalRecord newestRecord = getNewestRecord.get();
            //set the record id to the prescribed medication
            prescribedMedication.setMedicalRecordId(newestRecord.getId());
            return daoPrescribedMedication.save(prescribedMedication);
        } else {
            return Either.left(getNewestRecord.getLeft());
        }
    }
}
