package dao.xml.impl;

import common.Constants;
import common.config.Configuration;
import dao.xml.DaoPrescribedMedication;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.log4j.Log4j2;
import model.PrescribedMedication;
import model.error.AppError;
import model.xml.PatientXML;
import model.xml.PatientsXML;
import model.xml.PrescribedMedicationXML;
import model.xml.PrescribedMedicationsXML;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class DaoPrescribedMedicationImpl implements DaoPrescribedMedication {

    private final Configuration config;

    @Inject
    public DaoPrescribedMedicationImpl(Configuration config) {
        this.config = config;
    }


    //gets the prescribed medication from a specific patient
    @Override
    public Either<AppError, List<PrescribedMedicationXML>> get(PrescribedMedication prescribedMedication) {
        Either<AppError, List<PrescribedMedicationXML>> result;
        int patientId = prescribedMedication.getId();

        Either<AppError, PrescribedMedicationsXML> either = readXML(patientId);

        if(either.isLeft()){
            result = Either.left(either.getLeft());
        } else{
            PrescribedMedicationsXML medicationsXML = either.get();
            result = Either.right(medicationsXML.getPrescribedMedication());
        }
        return result;
    }

    private Either<AppError, PrescribedMedicationsXML> readXML(int patientId) {
        Either<AppError, PrescribedMedicationsXML> result;

        File file = new File(config.getPathXML());
        PrescribedMedicationsXML prescribedMedications = new PrescribedMedicationsXML();
        List<PrescribedMedicationXML> prescribedMedicationList = new ArrayList<>();

        if (!file.exists()) {
            result = Either.left(new AppError(Constants.FILE_DOES_NOT_EXIST_ERROR));
        } else {
            try {
                JAXBContext context = JAXBContext.newInstance(PatientsXML.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                Optional<PatientXML> matchingPatient;
                PatientsXML initialList = (PatientsXML) unmarshaller.unmarshal(file);

                matchingPatient = initialList.getPatients().stream().filter(patientXML -> patientXML.getId() == patientId).findFirst();

                if (matchingPatient.isEmpty()) {
                    result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NO_DATA));
                } else {
                    matchingPatient.ifPresent(patient -> {
                        patient.getMedicalRecords().getMedicalRecords().forEach(medicalRecord -> prescribedMedicationList.addAll(medicalRecord.getPrescribedMedication().getPrescribedMedication()));
                        prescribedMedications.setPrescribedMedication(prescribedMedicationList);
                    });
                    result = Either.right(prescribedMedications);
                }

            } catch (JAXBException e) {
                log.error(new AppError(Constants.ERROR_READING_XML + Constants.BLANK_SPACE + e.getMessage()));
                result = Either.left(new AppError(Constants.INTERNAL_ERROR));
            }
        }
        return result;
    }

}
