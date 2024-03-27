package services.impl;

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
import services.PatientsService;

import java.util.List;

public class PatientsServiceImpl implements PatientsService {

    private final DaoPatients daoPatients;
    private final DaoMedicalRecords daoMedicalRecords;
    private final DaoPrescribedMedication daoPrescribedMedication;

    @Inject
    public PatientsServiceImpl(DaoPatients daoPatients, DaoMedicalRecords daoMedicalRecords, DaoPrescribedMedication daoPrescribedMedication) {
        this.daoPatients = daoPatients;
        this.daoMedicalRecords = daoMedicalRecords;
        this.daoPrescribedMedication = daoPrescribedMedication;
    }

    @Override
    public Either<AppError, List<Patient>> getAll() {
        return daoPatients.getAll();
    }

    @Override
    public Either<AppError, Integer> delete(int patientId, Boolean confirmed) {
        Either<AppError, Integer> result;
        Patient patient = new Patient(patientId);

        //check if the patient has any medical records
        MedicalRecord objectRecord = new MedicalRecord(patientId);
        Either<AppError, List<MedicalRecord>> medicalRecords = daoMedicalRecords.getAll(objectRecord);

        if (!confirmed) {
            //here I have to check if the patient has any prescribed medications associated to their medical records
            if (medicalRecords.isRight() && !medicalRecords.get().isEmpty()) {
                List<MedicalRecord> medRecords = medicalRecords.get();
                //check each medical Record
                Either<AppError,Integer> medicationResult = checkPrescribedMedication(medRecords);

                if(medicationResult.isLeft()){
                    return medicationResult;
                }

            } else{
                //delete the medical records
                daoMedicalRecords.delete(objectRecord);
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
                daoMedicalRecords.delete(objectRecord);
            }
        }
        result = daoPatients.delete(patient);
        return result;
    }

    private Either<AppError,Integer> checkPrescribedMedication(List<MedicalRecord> medicalRecords){
        for (MedicalRecord medRecord : medicalRecords) {
            PrescribedMedication medication = new PrescribedMedication(medRecord.getId());
            Either<AppError, List<PrescribedMedication>> medicationList = daoPrescribedMedication.getAll(medication);
            if (medicationList.isRight() && !medicationList.get().isEmpty()) {
                return Either.left(new AppError(Constants.PATIENT_HAS_MEDICATION_ASSOCIATED_TO_MEDICAL_RECORDS_ERROR));
            }
        }
        return Either.right(1);
    }

}
