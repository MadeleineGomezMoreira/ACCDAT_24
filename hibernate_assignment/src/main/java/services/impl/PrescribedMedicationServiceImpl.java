package services.impl;

import dao.DaoPrescribedMedication;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.PrescribedMedication;
import model.error.AppError;

public class PrescribedMedicationServiceImpl implements services.PrescribedMedicationService {

    private final DaoPrescribedMedication daoPrescribedMedication;

    @Inject
    public PrescribedMedicationServiceImpl(DaoPrescribedMedication daoPrescribedMedication) {
        this.daoPrescribedMedication = daoPrescribedMedication;
    }

    @Override
    public Either<AppError,Integer> save(PrescribedMedication prescribedMedication) {
        return daoPrescribedMedication.save(prescribedMedication);
    }
}
