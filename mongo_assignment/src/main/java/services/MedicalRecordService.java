package services;

import io.vavr.control.Either;
import model.hibernate.MedicalRecordEntity;
import model.dto.PatientWithRecordsDTO;
import model.error.AppError;
import model.mongo.MedicalRecord;
import org.bson.types.ObjectId;

import java.util.List;

public interface MedicalRecordService {

    Either<AppError, List<PatientWithRecordsDTO>> getAllRecordsByPatient();

    Either<AppError, Integer> save(MedicalRecord medicalRecord);
}
