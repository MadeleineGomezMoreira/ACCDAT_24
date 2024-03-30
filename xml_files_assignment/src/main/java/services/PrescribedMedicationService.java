package services;

import io.vavr.control.Either;
import model.PrescribedMedication;
import model.error.AppError;

import java.util.List;

public interface PrescribedMedicationService {
    //gets the prescribed medication from a specific patient
    Either<AppError, List<PrescribedMedication>> getPatientMedication(int patientId);
}
