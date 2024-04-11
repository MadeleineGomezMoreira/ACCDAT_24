package services.impl;

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

    @Override
    public Either<AppError, List<MedicalRecord>> getAllRecordsByPatient(int patientId) {
        return daoMedicalRecord.getAll(new MedicalRecord(patientId));
    }

    //show all the medical records along with their prescribed medications
    @Override
    public Either<AppError, List<MedicalRecord>> showRecordsWithMedications() {
        Either<AppError, List<MedicalRecord>> getAllRecords = daoMedicalRecord.getAll(new MedicalRecord());
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
        Either<AppError, List<MedicalRecord>> oldMedicalRecords = daoMedicalRecord.getAll();
        if (oldMedicalRecords.isLeft()) {
            return Either.left(oldMedicalRecords.getLeft());
        }

        List<MedicalRecord> medicalRecords = oldMedicalRecords.get();

        Either<AppError, List<PrescribedMedication>> oldPrescribedMedications = daoPrescribedMedication.getAll();
        if (oldPrescribedMedications.isRight()) {
            List<PrescribedMedication> prescribedMedications = oldPrescribedMedications.get();

            //we group the prescribed medications by medical record id into a map
            Map<Integer, List<PrescribedMedication>> medicationMap = prescribedMedications.stream()
                    .collect(Collectors.groupingBy(PrescribedMedication::getMedicalRecordId));

            //we add the prescribed medications to the medical records
            medicalRecords.forEach(medicalRecord ->
                    medicalRecord.setPrescribedMedication(medicationMap.getOrDefault(medicalRecord.getId(), new ArrayList<>()))
            );
        }

        //we finally convert the medical records to XML and save them
        List<MedicalRecordXML> medicalRecordXMLList = medicalRecords.stream()
                .flatMap(this::toMedicalRecordXML)
                .toList();

        return daoXML.save(new MedicalRecordsXML(medicalRecordXMLList));
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

