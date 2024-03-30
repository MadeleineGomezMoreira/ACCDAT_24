package dao.xml;

import io.vavr.control.Either;
import model.error.AppError;
import model.xml.MedicalRecordXML;

public interface DaoMedicalRecord {
    Either<AppError, Integer> save(MedicalRecordXML medicalRecord, int patientId);
}
