package services.impl;

import dao.DaoPatient;
import dao.DaoPayment;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Patient;
import model.Payment;
import model.dto.PatientDTO;
import model.error.AppError;
import services.PatientService;

import java.util.ArrayList;
import java.util.List;

public class PatientServiceImpl implements PatientService {

    private final DaoPatient daoPatient;
    private final DaoPayment daoPayment;

    @Inject
    public PatientServiceImpl(DaoPatient daoPatient, DaoPayment daoPayment) {
        this.daoPatient = daoPatient;
        this.daoPayment = daoPayment;
    }

    @Override
    public Either<AppError, List<PatientDTO>> getAllWithAmountPaid() {
        Either<AppError, List<PatientDTO>> result;
        List<PatientDTO> patientsWithPayment = new ArrayList<>();
        Either<AppError, List<Patient>> getAllPatients = daoPatient.getAll();

        if (getAllPatients.isRight()) {
            List<Patient> patients = getAllPatients.get();
            patients.forEach(patient -> {
                Either<AppError, List<Payment>> patientPayments = daoPayment.getAll(new Payment(patient.getId()));
                if (patientPayments.isLeft()) {
                    patientsWithPayment.add(toPatientDTO(patient, 0));
                } else {
                    double amountPaid = patientPayments.get().stream().mapToDouble(Payment::getQuantity).sum();
                    patientsWithPayment.add(toPatientDTO(patient, amountPaid));
                }
            });
            result = Either.right(patientsWithPayment);
        } else {
            result = Either.left(getAllPatients.getLeft());
        }
        return result;
    }

    @Override
    public Either<AppError, Patient> getPatientWithMostMedicalRecords() {
        return daoPatient.get();
    }

    @Override
    public Either<AppError, Patient> getPatientByUsername(String username) {
        return daoPatient.get(username);
    }

    @Override
    public Either<AppError, List<Patient>> getAllByMedication(String medicationName) {
        return daoPatient.getAll(new Patient(medicationName));
    }

    @Override
    public Either<AppError, Integer> delete(int patientId, boolean confirmed) {
        return daoPatient.delete(new Patient(patientId), confirmed);
    }

    private PatientDTO toPatientDTO(Patient patient, double amountPaid) {
        return new PatientDTO(
                patient.getName(),
                patient.getBirthDate(),
                patient.getPhone(),
                amountPaid
        );
    }

}
