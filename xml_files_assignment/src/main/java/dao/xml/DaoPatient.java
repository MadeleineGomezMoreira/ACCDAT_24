package dao.xml;

import io.vavr.control.Either;
import model.Patient;
import model.error.AppError;
import model.xml.PatientXML;

import java.util.List;

public interface DaoPatient {
    Either<AppError, List<PatientXML>> getAll(Patient patient);

    Either<AppError, Integer> saveAll(List<PatientXML> patientXMLList);

    Either<AppError, Integer> delete(Patient patient, boolean confirmed);
}
