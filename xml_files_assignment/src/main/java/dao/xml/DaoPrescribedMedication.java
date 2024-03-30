package dao.xml;

import io.vavr.control.Either;
import model.PrescribedMedication;
import model.error.AppError;
import model.xml.PrescribedMedicationXML;

import java.util.List;

public interface DaoPrescribedMedication {
    //gets the prescribed medication from a specific patient
    Either<AppError, List<PrescribedMedicationXML>> get(PrescribedMedication prescribedMedication);
}
