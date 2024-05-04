package dao;

import io.vavr.control.Either;
import model.error.AppError;
import model.xml.MedicalRecordsXML;

public interface DaoXML {
    Either<AppError, Integer> save();
}
