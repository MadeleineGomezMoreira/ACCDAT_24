package services.impl;

import common.Constants;
import dao.*;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Doctor;
import model.MedicalRecord;
import model.Patient;
import model.PrescribedMedication;
import model.error.AppError;
import model.xml.MedicalRecordXML;
import model.xml.MedicalRecordsXML;
import model.xml.PrescribedMedicationXML;
import model.xml.PrescribedMedicationsXML;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MedicalRecordServiceImpl implements services.MedicalRecordService {

    private final DaoMedicalRecord daoMedicalRecord;
    private final DaoXML daoXML;
    private final DaoPatient daoPatient;
    private final DaoDoctor daoDoctor;
    private final DaoPrescribedMedication daoPrescribedMedication;

    @Inject
    public MedicalRecordServiceImpl(DaoMedicalRecord daoMedicalRecord, DaoXML daoXML, DaoPatient daoPatient, DaoDoctor daoDoctor, DaoPrescribedMedication daoPrescribedMedication) {
        this.daoMedicalRecord = daoMedicalRecord;
        this.daoXML = daoXML;
        this.daoPatient = daoPatient;
        this.daoDoctor = daoDoctor;
        this.daoPrescribedMedication = daoPrescribedMedication;
    }

    //show all the medical records along with their prescribed medications
    @Override
    public Either<AppError, List<MedicalRecord>> showRecordsWithMedications() {
        Either<AppError, List<MedicalRecord>> getAllRecords = daoMedicalRecord.getAll();
        Either<AppError, List<PrescribedMedication>> getAllMedication = daoPrescribedMedication.getAll();

        if (getAllRecords.isRight()) {
            List<MedicalRecord> medicalRecords = getAllRecords.get();
            if (getAllMedication.isRight()) {
                List<PrescribedMedication> prescribedMedications = getAllMedication.get();

                //we add the prescribed medication to the medical records
                medicalRecords.forEach(medRecord ->
                        medRecord.setPrescribedMedication(
                                prescribedMedications.stream()
                                        .filter(medication -> medication.getMedicalRecordId() == medRecord.getId())
                                        .toList())
                );

            }
            return Either.right(medicalRecords);
        } else {
            return Either.left(getAllRecords.getLeft());
        }
    }

    @Override
    public Either<AppError, Integer> deleteOldAndSaveXML() {
        if (daoXML.save().isRight()) {
            //then we delete them from the database (both medical records and prescribed medications)
            return daoMedicalRecord.delete();
        } else {
            return Either.left(new AppError(Constants.ERROR_SAVING_OLD_RECORDS));
        }
    }

    private Stream<MedicalRecordXML> toMedicalRecordXML(MedicalRecord medRecord) {
        Either<AppError, Doctor> doctors = daoDoctor.get(new Doctor(medRecord.getDoctorId()));
        Either<AppError, List<Patient>> patients = daoPatient.get(new Patient(medRecord.getPatientId()));

        if (doctors.isRight() && patients.isRight()) {
            Patient patient = patients.get().get(0);
            Doctor doctor = doctors.get();
            return Stream.of(new MedicalRecordXML(
                    patient.getName(),
                    medRecord.getAdmissionDate(),
                    medRecord.getDiagnosis(),
                    doctor.getName(),
                    new PrescribedMedicationsXML(toPrescribedMedicationXML(medRecord.getPrescribedMedication()))));
        } else {
            return Stream.empty();
        }
    }

    private List<PrescribedMedicationXML> toPrescribedMedicationXML(List<PrescribedMedication> medicationList) {
        return medicationList.stream()
                .map(medication -> new PrescribedMedicationXML(
                        medication.getName(),
                        medication.getDose()))
                .toList();
    }
}

