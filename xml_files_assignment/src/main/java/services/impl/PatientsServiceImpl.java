package services.impl;

import common.Constants;
import dao.txt.DaoMedicalRecords;
import dao.txt.DaoPatients;
import dao.txt.DaoPrescribedMedications;
import dao.xml.DaoPatient;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MedicalRecord;
import model.Patient;
import model.PrescribedMedication;
import model.error.AppError;
import model.xml.PatientXML;
import services.PatientsService;

import java.util.List;

public class PatientsServiceImpl implements PatientsService {

    private final DaoPatients daoPatients;
    private final DaoMedicalRecords daoMedicalRecords;
    private final DaoPrescribedMedications daoPrescribedMedications;

    private final DaoPatient daoPatient;

    @Inject
    public PatientsServiceImpl(DaoPatients daoPatients, DaoMedicalRecords daoMedicalRecords, DaoPrescribedMedications daoPrescribedMedications, DaoPatient daoPatient) {
        this.daoPatients = daoPatients;
        this.daoMedicalRecords = daoMedicalRecords;
        this.daoPrescribedMedications = daoPrescribedMedications;
        this.daoPatient = daoPatient;
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
                Either<AppError, Integer> medicationResult = checkPrescribedMedication(medRecords);

                if (medicationResult.isLeft()) {
                    return medicationResult;
                }

            } else {
                //delete the medical records
                daoMedicalRecords.delete(objectRecord);
            }
        } else {
            if (medicalRecords.isRight() && !medicalRecords.get().isEmpty()) {

                List<MedicalRecord> records = medicalRecords.get();

                //for each medical record, delete the associated prescribed medication (one or several) by record id
                for (MedicalRecord medicalRecord : records) {
                    PrescribedMedication medication = new PrescribedMedication(medicalRecord.getId());
                    daoPrescribedMedications.delete(medication);
                }
                //delete the medical records
                daoMedicalRecords.delete(objectRecord);
            }
        }
        result = daoPatients.delete(patient);
        return result;
    }

    @Override
    public Either<AppError, Integer> deleteXML(int patientId, Boolean confirmed) {
        Either<AppError, Integer> result;
        Patient patient = new Patient(patientId);
        result = daoPatient.delete(patient, confirmed);
        return result;
    }

    @Override
    public Either<AppError, List<Patient>> getPatientsByMedication(String medicationName) {
        Either<AppError, List<Patient>> result;
        Either<AppError, List<PatientXML>> either = daoPatient.getAll(new Patient(medicationName));
        if (either.isRight()) {
            result = fromXML(either.get());
        } else {
            result = Either.left(either.getLeft());
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> writeAllPatientsInXML() {
        Either<AppError, Integer> result;
        Either<AppError, List<PatientXML>> allPatientsEither = daoPatient.getAll(new Patient(""));
        if (allPatientsEither.isRight()) {
            result = daoPatient.saveAll(allPatientsEither.get());
        } else {
            result = Either.left(allPatientsEither.getLeft());
        }
        return result;
    }

    private Either<AppError, List<Patient>> fromXML(List<PatientXML> patientXMLList) {
        List<Patient> patientList = patientXMLList.stream().map(patientXML ->
                new Patient(
                        patientXML.getId(),
                        patientXML.getName(),
                        patientXML.getBirthDate(),
                        patientXML.getPhone()
                )).toList();

        if (patientList.isEmpty()) {
            return Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
        } else {
            return Either.right(patientList);
        }
    }

    private Either<AppError, Integer> checkPrescribedMedication(List<MedicalRecord> medicalRecords) {
        for (MedicalRecord medRecord : medicalRecords) {
            PrescribedMedication medication = new PrescribedMedication(medRecord.getId());
            Either<AppError, List<PrescribedMedication>> medicationList = daoPrescribedMedications.getAll(medication);
            if (medicationList.isRight() && !medicationList.get().isEmpty()) {
                return Either.left(new AppError(Constants.PATIENT_HAS_MEDICATION_ASSOCIATED_TO_MEDICAL_RECORDS_ERROR));
            }
        }
        return Either.right(1);
    }

}
