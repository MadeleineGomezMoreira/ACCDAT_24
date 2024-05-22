package dao;

import io.vavr.control.Either;
import model.Payment;
import model.error.AppError;

import java.util.List;

public interface DaoPayment {
    Either<AppError, List<Payment>> getAll();
}
