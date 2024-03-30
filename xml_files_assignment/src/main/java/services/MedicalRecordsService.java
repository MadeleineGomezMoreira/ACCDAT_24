package services;

import io.vavr.control.Either;
import model.MedicalRecord;
import model.dtos.MedicalRecordDTO;
import model.error.AppError;

import java.util.List;

public interface MedicalRecordsService {
    //show medical record by patient
    Either<AppError, List<MedicalRecord>> getAll(int patientId);

    //save medical record with medications
    Either<AppError, Integer> save(MedicalRecordDTO medRecord);

    //save medical record to XML file
    Either<AppError, Integer> saveXML(MedicalRecordDTO medRecord);
}
