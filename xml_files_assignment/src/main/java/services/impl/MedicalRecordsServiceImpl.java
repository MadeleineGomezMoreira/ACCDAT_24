package services.impl;

import common.Constants;
import dao.txt.DaoDoctors;
import dao.txt.DaoMedicalRecords;
import dao.txt.DaoPatients;
import dao.txt.DaoPrescribedMedications;
import dao.xml.DaoMedicalRecord;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Doctor;
import model.MedicalRecord;
import model.Patient;
import model.PrescribedMedication;
import model.dtos.MedicalRecordDTO;
import model.error.AppError;
import model.xml.MedicalRecordXML;
import model.xml.PrescribedMedicationXML;
import model.xml.PrescribedMedicationsXML;
import services.MedicalRecordsService;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordsServiceImpl implements MedicalRecordsService {
    private final DaoMedicalRecords daoMedicalRecords;
    private final DaoPrescribedMedications daoPrescribedMedications;
    private final DaoDoctors daoDoctors;
    private final DaoPatients daoPatients;
    private final DaoMedicalRecord daoMedicalRecord;

    @Inject
    public MedicalRecordsServiceImpl(DaoMedicalRecords daoMedicalRecords, DaoPrescribedMedications daoPrescribedMedications, DaoDoctors daoDoctors, DaoPatients daoPatients, DaoMedicalRecord daoMedicalRecord) {
        this.daoMedicalRecords = daoMedicalRecords;
        this.daoPrescribedMedications = daoPrescribedMedications;
        this.daoDoctors = daoDoctors;
        this.daoPatients = daoPatients;
        this.daoMedicalRecord = daoMedicalRecord;
    }

    //show medical record by patient
    @Override
    public Either<AppError, List<MedicalRecord>> getAll(int patientId) {
        return daoMedicalRecords.getAll(new MedicalRecord(patientId));
    }

    //save medical record with medications
    @Override
    public Either<AppError, Integer> save(MedicalRecordDTO medRecord) {
        Either<AppError, Integer> result;

        Either<AppError, Doctor> doctorExists = daoDoctors.get(new Doctor(medRecord.getDoctorId()));
        Either<AppError, Patient> patientExists = daoPatients.get(new Patient(medRecord.getPatientId()));

        //check if doctor and patient exist first
        if (doctorExists.isLeft() || patientExists.isLeft()) {
            result = Either.left(new AppError(Constants.DOCTOR_OR_PATIENT_DO_NOT_EXIST_ERROR));
        } else {
            Either<AppError, Integer> either = daoMedicalRecords.save(new MedicalRecord(medRecord.getId(), medRecord.getAdmissionDate(), medRecord.getDiagnosis(), medRecord.getPatientId(), medRecord.getDoctorId()

            ));
            if (either.isRight()) {
                List<PrescribedMedication> medicationList = medRecord.getMedicationList();

                for (PrescribedMedication medication : medicationList) {
                    medication.setMedicalRecordId(either.get());
                    Either<AppError, Integer> additionResult = daoPrescribedMedications.save(medication);
                    if (additionResult.isLeft()) {
                        return Either.left(additionResult.getLeft());
                    }
                }
                result = Either.right(1);
            } else {
                result = Either.left(either.getLeft());
            }
        }
        return result;
    }

    //save medical record to XML file
    @Override
    public Either<AppError, Integer> saveXML(MedicalRecordDTO medRecord) {
        Either<AppError, Integer> result;

        Either<AppError, Doctor> doctorExists = daoDoctors.get(new Doctor(medRecord.getDoctorId()));
        Either<AppError, Patient> patientExists = daoPatients.get(new Patient(medRecord.getPatientId()));

        assignLastId(medRecord);

        //check if doctor and patient exist first
        if (doctorExists.isLeft() || patientExists.isLeft()) {
            result = Either.left(new AppError(Constants.DOCTOR_OR_PATIENT_DO_NOT_EXIST_ERROR));
        } else {
            List<PrescribedMedication> medicationList = medRecord.getMedicationList();
            List<PrescribedMedicationXML> medicationXMLList = new ArrayList<>();

            for (PrescribedMedication medication : medicationList) {
                medicationXMLList.add(new PrescribedMedicationXML(medication.getName(), medication.getDose()));
            }

            PrescribedMedicationsXML prescribedMedicationsXML = new PrescribedMedicationsXML(medicationXMLList);

            MedicalRecordXML recordXML = new MedicalRecordXML(medRecord.getAdmissionDate(), medRecord.getDiagnosis(), doctorExists.get().getName(), prescribedMedicationsXML);

            Either<AppError, Integer> either = daoMedicalRecord.save(recordXML, medRecord.getPatientId());
            if (either.isRight()) {
                result = Either.right(1);
            } else {
                result = Either.left(either.getLeft());
            }
        }
        return result;
    }

    private void assignLastId(MedicalRecordDTO medicalRecord) {
        medicalRecord.setId(daoMedicalRecords.getAll().get().stream().mapToInt(MedicalRecord::getId).max().orElse(0) + 1);
        List<PrescribedMedication>  medicationList = medicalRecord.getMedicationList();
        for (PrescribedMedication medication : medicationList) {
            medication.setId(daoPrescribedMedications.getAll().get().stream().mapToInt(PrescribedMedication::getId).max().orElse(0) + 1);
        }
    }


}
