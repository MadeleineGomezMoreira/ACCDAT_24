package services.impl;

import common.Constants;
import dao.DaoMedicalRecords;
import dao.DaoPatients;
import dao.DaoPrescribedMedication;
import dao.DaoDoctors;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Doctor;
import model.MedicalRecord;
import model.Patient;
import model.PrescribedMedication;
import model.dtos.MedicalRecordDTO;
import model.error.AppError;
import services.MedicalRecordsService;

import java.util.List;

public class MedicalRecordsServiceImpl implements MedicalRecordsService {

    private final DaoMedicalRecords daoMedicalRecords;
    private final DaoPrescribedMedication daoPrescribedMedication;
    private final DaoDoctors daoDoctors;
    private final DaoPatients daoPatients;

    @Inject
    public MedicalRecordsServiceImpl(DaoMedicalRecords daoMedicalRecords, DaoPrescribedMedication daoPrescribedMedication, DaoDoctors daoDoctors, DaoPatients daoPatients) {
        this.daoMedicalRecords = daoMedicalRecords;
        this.daoPrescribedMedication = daoPrescribedMedication;
        this.daoDoctors = daoDoctors;
        this.daoPatients = daoPatients;
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
            Either<AppError, Integer> either = daoMedicalRecords.save(new MedicalRecord(
                    medRecord.getId(),
                    medRecord.getAdmissionDate(),
                    medRecord.getDiagnosis(),
                    medRecord.getPatientId(),
                    medRecord.getDoctorId()

            ));
            if (either.isRight()) {
                List<PrescribedMedication> medicationList = medRecord.getMedicationList();

                for (PrescribedMedication medication : medicationList) {
                    medication.setMedicalRecordId(either.get());
                    Either<AppError, Integer> additionResult = daoPrescribedMedication.save(medication);
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


}
