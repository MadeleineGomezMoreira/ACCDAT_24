package dao.impl;

import common.Constants;
import common.config.Config;
import dao.DaoDoctor;
import dao.DaoPatient;
import dao.common.QueryStrings;
import dao.connection.DBConnectionPool;
import dao.mappers.MedicalRecordMapper;
import dao.mappers.PrescribedMedicationMapper;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import lombok.extern.log4j.Log4j2;
import model.Doctor;
import model.MedicalRecord;
import model.Patient;
import model.PrescribedMedication;
import model.error.AppError;
import model.xml.MedicalRecordXML;
import model.xml.MedicalRecordsXML;
import model.xml.PrescribedMedicationXML;
import model.xml.PrescribedMedicationsXML;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class DaoXMLImpl implements dao.DaoXML {

    private final Config config;
    private final DBConnectionPool pool;
    private final DaoDoctor daoDoctor;
    private final DaoPatient daoPatient;

    @Inject
    public DaoXMLImpl(Config config, DBConnectionPool pool, DaoDoctor daoDoctor, DaoPatient daoPatient) {
        this.config = config;
        this.pool = pool;
        this.daoDoctor = daoDoctor;
        this.daoPatient = daoPatient;
    }

    @Override
    public Either<AppError, Integer> save() {
        Either<AppError, MedicalRecordsXML> medicalRecordsXMLEither = getXMLToSave();
        if (medicalRecordsXMLEither.isLeft()) {
            return Either.left(medicalRecordsXMLEither.getLeft());
        }
        return writeXML(getXMLToSave().get());
    }

    private Either<AppError, Integer> writeXML(MedicalRecordsXML medicalRecordsXML) {
        Either<AppError, Integer> result;

        File file = new File(config.getProperty(Constants.PATH_XML));

        if (!file.exists()) {
            result = Either.left(new AppError(Constants.FILE_DOES_NOT_EXIST_ERROR));
        } else {

            try {
                JAXBContext context = JAXBContext.newInstance(MedicalRecordsXML.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                marshaller.marshal(medicalRecordsXML, file);

                return Either.right(1);
            } catch (Exception e) {
                log.error(new AppError(Constants.ERROR_WRITING_XML + Constants.BLANK_SPACE + e.getMessage()));
                return Either.left(new AppError(Constants.INTERNAL_ERROR));
            }
        }
        return result;
    }

    private Either<AppError, List<PrescribedMedication>> getAllMedication() {
        Either<AppError, List<PrescribedMedication>> result;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            List<PrescribedMedication> listPrescribedMedication = jdbcTemplate.query(QueryStrings.GET_ALL_OLD_PRESCRIBED_MEDICATIONS, new PrescribedMedicationMapper());
            if (listPrescribedMedication.isEmpty()) {
                return Either.left(new AppError(Constants.NO_MEDICATION_PRESCRIBED_TO_PATIENT_ERROR));
            } else {
                result = Either.right(listPrescribedMedication);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Either.left(new AppError(e.getMessage()));
        }
        return result;
    }

    private Either<AppError, List<MedicalRecord>> getAllRecords() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            List<MedicalRecord> medicalRecords = jdbcTemplate.query(QueryStrings.GET_ALL_OLD_MEDICAL_RECORDS, new MedicalRecordMapper());
            return Either.right(medicalRecords);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Either.left(new AppError(e.getMessage()));
        }
    }

    private Either<AppError, MedicalRecordsXML> getXMLToSave() {
        Either<AppError, List<MedicalRecord>> oldMedicalRecords = getAllRecords();
        if (oldMedicalRecords.isLeft()) {
            return Either.left(oldMedicalRecords.getLeft());
        }

        List<MedicalRecord> medicalRecords = oldMedicalRecords.get();

        if (medicalRecords.isEmpty()) {
            return Either.left(new AppError(Constants.NO_OLD_RECORDS_FOUND));
        }

        Either<AppError, List<PrescribedMedication>> oldPrescribedMedications = getAllMedication();
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


        return Either.right(new MedicalRecordsXML(medicalRecordXMLList));
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
