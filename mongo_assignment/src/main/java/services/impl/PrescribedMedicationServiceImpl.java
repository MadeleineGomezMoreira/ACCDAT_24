package services.impl;

import dao.DaoPrescribedMedication;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.PrescribedMedication;
import model.dto.PrescribedMedicationDTO;
import model.dto.RecordWithPrescriptionsDTO;
import model.error.AppError;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PrescribedMedicationServiceImpl implements services.PrescribedMedicationService {

    private final DaoPrescribedMedication daoPrescribedMedication;

    @Inject
    public PrescribedMedicationServiceImpl(DaoPrescribedMedication daoPrescribedMedication) {
        this.daoPrescribedMedication = daoPrescribedMedication;
    }

    @Override
    public Either<AppError, Integer> save(PrescribedMedication prescribedMedication) {
        return daoPrescribedMedication.save(prescribedMedication);
    }

    @Override
    public Either<AppError, List<RecordWithPrescriptionsDTO>> getRecordsWithPrescription() {
        Either<AppError, List<RecordWithPrescriptionsDTO>> result;

        Either<AppError, List<PrescribedMedication>> prescriptions = daoPrescribedMedication.getAll();

        if (prescriptions.isLeft()) {
            result = Either.left(prescriptions.getLeft());
        } else {

            List<PrescribedMedication> medication = prescriptions.get();
            List<RecordWithPrescriptionsDTO> recordsWithPrescriptions = new ArrayList<>();

            medication.stream()
                    .sorted(Comparator.comparingInt(PrescribedMedication::getMedicalRecordId))
                    .forEach(prescribedMedication -> {
                        RecordWithPrescriptionsDTO recordWithPrescriptions = new RecordWithPrescriptionsDTO();
                        recordWithPrescriptions.setRecordId(prescribedMedication.getMedicalRecordId());
                        List<PrescribedMedicationDTO> prescriptionsList = recordWithPrescriptions.getPrescription();
                        prescriptionsList.add(new PrescribedMedicationDTO(prescribedMedication.getName(), prescribedMedication.getDose()));
                        recordWithPrescriptions.setPrescription(prescriptionsList);
                        recordsWithPrescriptions.add(recordWithPrescriptions);
                    });
            result = Either.right(recordsWithPrescriptions);
        }
        return result;
    }
}
