package dao;

import io.vavr.control.Either;
import model.Payment;
import model.error.AppError;

import java.util.List;

public interface DaoPayment {
    //get all the payments done by a specific patient
    Either<AppError, List<Payment>> getAll(Payment payment);
}
