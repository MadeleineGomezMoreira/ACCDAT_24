package services.impl;

import common.Constants;
import dao.xml.DaoPrescribedMedication;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.PrescribedMedication;
import model.error.AppError;
import model.xml.PrescribedMedicationXML;
import services.PrescribedMedicationService;

import java.util.List;

public class PrescribedMedicationServiceImpl implements PrescribedMedicationService {

    private final DaoPrescribedMedication daoPrescribedMedication;

    @Inject
    public PrescribedMedicationServiceImpl(DaoPrescribedMedication daoPrescribedMedication) {
        this.daoPrescribedMedication = daoPrescribedMedication;
    }

    //gets the prescribed medication from a specific patient
    @Override
    public Either<AppError, List<PrescribedMedication>> getPatientMedication(int patientId) {
        Either<AppError, List<PrescribedMedication>> result;
        Either<AppError, List<PrescribedMedicationXML>> either = daoPrescribedMedication.get(new PrescribedMedication(patientId));

        if (either.isLeft()) {
            result = Either.left(either.getLeft());
        } else {
            List<PrescribedMedicationXML> prescribedMedicationXMLList = either.get();
            Either<AppError, List<PrescribedMedication>> medResult = fromXML(prescribedMedicationXMLList);
            if (medResult.isLeft()) {
                result = Either.left(medResult.getLeft());
            } else {
                result = Either.right(medResult.get());
            }
        }
        return result;
    }

    private Either<AppError, List<PrescribedMedication>> fromXML(List<PrescribedMedicationXML> prescribedMedicationXMLList) {
        List<PrescribedMedication> medicationList = prescribedMedicationXMLList.stream().map(prescribedMedicationXML ->
                new PrescribedMedication(prescribedMedicationXML.getName(), prescribedMedicationXML.getDosage())
        ).toList();

        if (medicationList.isEmpty()) {
            return Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
        } else {
            return Either.right(medicationList);
        }
    }

}
