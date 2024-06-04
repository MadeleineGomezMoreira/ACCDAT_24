package services;

import io.vavr.control.Either;
import model.dto.PatientWithMedicationDTO;
import model.hibernate.PrescribedMedicationEntity;
import model.dto.RecordWithPrescriptionsDTO;
import model.error.AppError;
import model.mongo.PrescribedMedication;
import org.bson.types.ObjectId;

import java.util.List;

public interface PrescribedMedicationService {

    Either<AppError, PatientWithMedicationDTO> getMedicationFromSpecificPatient(ObjectId objectId);

    Either<AppError, Integer> save(PrescribedMedication medication);

    Either<AppError, Integer> deleteLastMedicationFromLastMedicalRecord(ObjectId patientId);
}
