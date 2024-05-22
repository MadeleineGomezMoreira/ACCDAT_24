package services;

import io.vavr.control.Either;
import model.dto.TotalPaymentByPatientDTO;
import model.error.AppError;

import java.util.List;

public interface PaymentService {
    Either<AppError, List<TotalPaymentByPatientDTO>> getTotalPaymentByPatient();
}
