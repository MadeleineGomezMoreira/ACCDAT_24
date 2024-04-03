package dao.xml.impl;

import common.Constants;
import common.config.Configuration;
import dao.xml.DaoPatient;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.log4j.Log4j2;
import model.Patient;
import model.error.AppError;
import model.xml.PatientXML;
import model.xml.PatientsXML;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class DaoPatientImpl implements DaoPatient {

    private final Configuration config;

    @Inject
    public DaoPatientImpl(Configuration config) {
        this.config = config;
    }

    @Override
    public Either<AppError, List<PatientXML>> getAll(Patient patient) {


        if (patient.getName() != null) {
            String medicationName = patient.getName();
            return readXML(medicationName, false).flatMap(patientsXML -> {
                        if (patientsXML.getPatients().isEmpty()) {
                            return Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
                        } else {
                            return Either.right(patientsXML.getPatients());
                        }
                    }
            );
        } else {
            return readXML(null, true).flatMap(patientsXML -> {
                        if (patientsXML.getPatients().isEmpty()) {
                            return Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NO_DATA));
                        } else {
                            return Either.right(patientsXML.getPatients());
                        }
                    }
            );
        }
    }

    @Override
    public Either<AppError, Integer> saveAll(List<PatientXML> patientXMLList) {
        PatientsXML patientsXML = new PatientsXML();
        patientsXML.setPatients(patientXMLList);

        return writeXML(patientsXML);
    }

    @Override
    public Either<AppError, Integer> delete(Patient patient, boolean confirmed) {
        int patientId = patient.getId();

        return readXML(Constants.EMPTY_STRING, true).flatMap(patientsXML -> {

            List<PatientXML> patientXMLList = patientsXML.getPatients();

            Optional<PatientXML> patientOptional = patientXMLList.stream()
                    .filter(patientXML -> patientXML.getId() == patientId)
                    .findFirst();

            if (patientOptional.isPresent()) {
                PatientXML patientXML = patientOptional.get();

                if (patientXML.getMedicalRecords() != null) {
                    //check if prescribed medication is null for the patient
                    AtomicBoolean patientHasMedication = new AtomicBoolean(false);

                    patientXML.getMedicalRecords().getMedicalRecords().forEach(medicalRecordXML -> {
                        if (medicalRecordXML.getPrescribedMedication() != null) {
                            patientHasMedication.set(true);
                        }
                    });

                    if (patientHasMedication.get() && !confirmed) {
                        return Either.left(new AppError(Constants.PATIENT_HAS_MEDICATION_ASSOCIATED_TO_MEDICAL_RECORDS_ERROR));
                    }
                }

                List<PatientXML> updatedPatientList = patientXMLList.stream()
                        .filter(patientToDelete -> patientToDelete.getId() != patientId)
                        .toList();

                patientsXML.setPatients(updatedPatientList);

                Either<AppError, Integer> writeResult = writeXML(patientsXML);

                if (writeResult.isLeft()) {
                    return writeResult;
                }

                return Either.right(1);

            } else return Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
        });
    }

    private Either<AppError, PatientsXML> readXML(String medicationName, boolean allPatients) {
        Either<AppError, PatientsXML> result;
        PatientsXML patients = new PatientsXML();

        File file = new File(config.getPathXML());

        if (!file.exists()) {
            result = Either.left(new AppError(Constants.FILE_DOES_NOT_EXIST_ERROR));
        } else {
            try {
                JAXBContext context = JAXBContext.newInstance(PatientsXML.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                List<PatientXML> matchingPatients;
                PatientsXML initialList = (PatientsXML) unmarshaller.unmarshal(file);

                if (!allPatients) {
                    matchingPatients = initialList.getPatients().stream()
                            .filter(patient -> patient.getMedicalRecords().getMedicalRecords().stream().anyMatch(
                                    medRecord -> medRecord.getPrescribedMedication().getPrescribedMedication().stream().anyMatch(
                                            medication -> medication.getName().equals(medicationName)
                                    )
                            )).toList();
                } else {
                    matchingPatients = initialList.getPatients();
                }

                patients.setPatients(matchingPatients);

                if (patients.getPatients() == null) {
                    result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NO_DATA));
                } else {
                    result = Either.right(patients);
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
