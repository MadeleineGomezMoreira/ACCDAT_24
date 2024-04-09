package services.impl;

import dao.DaoPrescribedMedication;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.PrescribedMedication;
import model.error.AppError;

import java.util.List;

public class PrescribedMedicationServiceImpl implements services.PrescribedMedicationService {

    private DaoPrescribedMedication daoPrescribedMedication;

    @Inject
    public PrescribedMedicationServiceImpl(DaoPrescribedMedication daoPrescribedMedication) {
        this.daoPrescribedMedication = daoPrescribedMedication;
    }

    @Override
    public Either<AppError, List<PrescribedMedication>> getPrescribedMedicationByPatient(int patientId) {
        return daoPrescribedMedication.getAll(new PrescribedMedication(patientId));
    }
}
