package services;

import common.Constants;
import dao.DaoMedicalRecords;
import dao.DaoPatients;
import dao.DaoPrescribedMedication;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MedicalRecord;
import model.Patient;
import model.PrescribedMedication;
import model.error.AppError;

import java.util.List;

public class PatientsServiceImpl {

    private final DaoPatients daoPatients;
    private final DaoMedicalRecords daoMedicalRecords;
    private final DaoPrescribedMedication daoPrescribedMedication;

    @Inject
    public PatientsServiceImpl(DaoPatients daoPatients, DaoMedicalRecords daoMedicalRecords, DaoPrescribedMedication daoPrescribedMedication) {
        this.daoPatients = daoPatients;
        this.daoMedicalRecords = daoMedicalRecords;
        this.daoPrescribedMedication = daoPrescribedMedication;
    }

    public Either<AppError, List<Patient>> getAll() {
        return daoPatients.getAll();
    }

    public Either<AppError, Integer> delete(int patientId, Boolean confirmed) {
        Either<AppError, Integer> result;
        Patient patient = new Patient(patientId);

        //check if the patient has any medical records
        MedicalRecord record = new MedicalRecord(patientId);
        Either<AppError, List<MedicalRecord>> medicalRecords = daoMedicalRecords.getAll(record);

        if (!confirmed) {
            if (medicalRecords.isRight() && !medicalRecords.get().isEmpty()) {
                return Either.left(new AppError(Constants.PATIENT_HAS_MEDICAL_RECORDS_ERROR));
            }
        } else {
            if (medicalRecords.isRight() && !medicalRecords.get().isEmpty()) {

                List<MedicalRecord> records = medicalRecords.get();

                //for each medical record, delete the associated prescribed medication (one or several) by record id
                for (MedicalRecord medicalRecord : records) {
                    PrescribedMedication medication = new PrescribedMedication(medicalRecord.getId());
                    daoPrescribedMedication.delete(medication);
                }
                //delete the medical records
                daoMedicalRecords.delete(record);
            }
        }
        result = daoPatients.delete(patient);
        return result;
    }

}
