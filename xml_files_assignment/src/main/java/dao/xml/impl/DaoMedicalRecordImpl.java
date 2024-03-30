package dao.xml.impl;

import common.Constants;
import common.config.Configuration;
import dao.xml.DaoMedicalRecord;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.log4j.Log4j2;
import model.error.AppError;
import model.xml.MedicalRecordXML;
import model.xml.MedicalRecordsXML;
import model.xml.PatientXML;
import model.xml.PatientsXML;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
public class DaoMedicalRecordImpl implements DaoMedicalRecord {

    private final Configuration config;

    @Inject
    public DaoMedicalRecordImpl(Configuration config) {
        this.config = config;
    }


    @Override
    public Either<AppError, Integer> save(MedicalRecordXML medicalRecord, int patientId) {
        Either<AppError, Integer> result;
        Either<AppError, PatientsXML> eitherOne = readXML(patientId, false);
        Either<AppError, PatientsXML> eitherAll = readXML(patientId, true);

        if (eitherOne.isRight() && eitherAll.isRight()) {

            PatientsXML patientsXML = eitherAll.get();
            PatientXML patient = eitherOne.get().getPatients().get(0);

            List<PatientXML> patientsList = new ArrayList<>(eitherAll.get().getPatients());

            List<MedicalRecordXML> records = new ArrayList<>(patient.getMedicalRecords().getMedicalRecords());
            records.add(medicalRecord);

            MedicalRecordsXML medRecordsList = new MedicalRecordsXML();
            medRecordsList.setMedicalRecords(records);

            patient.setMedicalRecords(medRecordsList);

            patientsList = patientsList.stream()
                    .filter(patientXML -> patientXML.getId() != patientId)
                    .collect(Collectors.toList());
            patientsList.add(patient);


            patientsXML.setPatients(patientsList);

            result = writeXML(patientsXML);
        } else {
            result = Either.left(eitherOne.getLeft());
        }
        return result;
    }

    private Either<AppError, PatientsXML> readXML(int patientId, boolean allPatients) {
        Either<AppError, PatientsXML> result;

        File file = new File(config.getPathXML());
        PatientsXML patients = new PatientsXML();

        if (!file.exists()) {
            result = Either.left(new AppError(Constants.FILE_DOES_NOT_EXIST_ERROR));
        } else {
            try {
                JAXBContext context = JAXBContext.newInstance(PatientsXML.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                Optional<PatientXML> matchingPatient;
                PatientsXML initialList = (PatientsXML) unmarshaller.unmarshal(file);


                if (!allPatients) {
                    matchingPatient = initialList.getPatients().stream()
                            .filter(patient -> patient.getId() == patientId
                            ).findFirst();

                    if (matchingPatient.isPresent()) {
                        List<PatientXML> patientXMLList = new ArrayList<>();
                        patientXMLList.add(matchingPatient.get());
                        patients.setPatients(patientXMLList);
                        result = Either.right(patients);
                    } else {
                        result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
                    }
                } else {

                    result = Either.right(initialList);

                }

            } catch (JAXBException e) {
                log.error(new AppError(Constants.ERROR_READING_XML + Constants.BLANK_SPACE + e.getMessage()));
                result = Either.left(new AppError(Constants.INTERNAL_ERROR));
            }
        }
        return result;
    }

    private Either<AppError, Integer> writeXML(PatientsXML patientsXML) {
        Either<AppError, Integer> result;

        File file = new File(config.getPathXML());

        if (!file.exists()) {
            result = Either.left(new AppError(Constants.FILE_DOES_NOT_EXIST_ERROR));
        } else {

            try {
                JAXBContext context = JAXBContext.newInstance(PatientsXML.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                marshaller.marshal(patientsXML, file);

                return Either.right(1);
            } catch (Exception e) {
                log.error(new AppError(Constants.ERROR_READING_XML + Constants.BLANK_SPACE + e.getMessage()));
                return Either.left(new AppError(Constants.INTERNAL_ERROR));
            }
        }
        return result;
    }


}
