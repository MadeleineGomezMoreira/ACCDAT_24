package services.impl;

import common.Constants;
import dao.DaoPatient;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Patient;
import model.error.AppError;

import java.time.LocalDate;
import java.util.List;

public class PatientServiceImpl implements services.PatientService {

    private final DaoPatient daoPatient;

    @Inject
    public PatientServiceImpl(DaoPatient daoPatient) {
        this.daoPatient = daoPatient;
    }

    @Override
    public Either<AppError, List<Patient>> getAllPatients() {
        return daoPatient.getAll();
    }

    @Override
    public Either<AppError, Patient> getPatientById(int id) {
        Patient patient = new Patient(id);
        return daoPatient.get(patient);
    }

    @Override
    public Either<AppError, Integer> updatePatient(Patient patient) {
        Either<AppError, Integer> result;

        if (patient.getName() == null || patient.getName().isEmpty()) {
            result = Either.left(new AppError(Constants.INVALID_NAME_INPUT_ERROR));
        } else if (patient.getBirthDate() == null || patient.getBirthDate().isAfter(LocalDate.now())) {
            result = Either.left(new AppError(Constants.INVALID_DATE_INPUT_ERROR));
        } else if (patient.getPhone() == null || patient.getPhone().length() != 9 || !patient.getPhone().matches("\\d+")) {
            result = Either.left(new AppError(Constants.INVALID_PHONE_INPUT_ERROR));
        } else {
            result = daoPatient.update(patient);
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> savePatient(Patient patient) {
        return daoPatient.save(patient);
    }

    @Override
    public Either<AppError, Integer> deletePatient(int id, Boolean confirmation) {
        return daoPatient.delete(new Patient(id), confirmation);
    }
}
