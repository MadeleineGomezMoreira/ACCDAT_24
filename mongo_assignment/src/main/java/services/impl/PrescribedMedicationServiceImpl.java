package services.impl;

import dao.hibernate.DaoPrescribedMedication;
import dao.mongo.DaoPrescribedMedicationMongo;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.dto.PatientWithMedicationDTO;
import model.hibernate.PrescribedMedicationEntity;
import model.dto.PrescribedMedicationDTO;
import model.dto.RecordWithPrescriptionsDTO;
import model.error.AppError;
import model.mongo.PrescribedMedication;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PrescribedMedicationServiceImpl implements services.PrescribedMedicationService {

    private final DaoPrescribedMedication daoPrescribedMedication;
    private final DaoPrescribedMedicationMongo daoPrescribedMedicationMongo;

    @Inject
    public PrescribedMedicationServiceImpl(DaoPrescribedMedication daoPrescribedMedication, DaoPrescribedMedicationMongo daoPrescribedMedicationMongo) {
        this.daoPrescribedMedication = daoPrescribedMedication;
        this.daoPrescribedMedicationMongo = daoPrescribedMedicationMongo;
    }

    @Override
    public Either<AppError, PatientWithMedicationDTO> getMedicationFromSpecificPatient(ObjectId objectId) {
        Either<AppError, List<PrescribedMedication>> medication = daoPrescribedMedicationMongo.getAll(objectId);

        if (medication.isLeft()) {
            return Either.left(medication.getLeft());
        } else {
            List<PrescribedMedication> prescribedMedications = medication.get();
            List<PrescribedMedicationDTO> prescribedMedicationDTOList = new ArrayList<>();
            prescribedMedications.forEach(prescribedMedication -> prescribedMedicationDTOList.add(new PrescribedMedicationDTO(prescribedMedication.getName(), prescribedMedication.getDose())));
            return Either.right(new PatientWithMedicationDTO(objectId, prescribedMedicationDTOList));
        }
    }

    @Override
    public Either<AppError, Integer> save(PrescribedMedicationEntity prescribedMedicationEntity) {
        return daoPrescribedMedication.save(prescribedMedicationEntity);
    }

    @Override
    public Either<AppError, List<RecordWithPrescriptionsDTO>> getRecordsWithPrescription() {
        Either<AppError, List<RecordWithPrescriptionsDTO>> result;

        Either<AppError, List<PrescribedMedicationEntity>> prescriptions = daoPrescribedMedication.getAll();

        if (prescriptions.isLeft()) {
            result = Either.left(prescriptions.getLeft());
        } else {

            List<PrescribedMedicationEntity> medication = prescriptions.get();
            List<RecordWithPrescriptionsDTO> recordsWithPrescriptions = new ArrayList<>();

            medication.stream()
                    .sorted(Comparator.comparingInt(PrescribedMedicationEntity::getMedicalRecordId))
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
