package dao.hibernate;

import io.vavr.control.Either;
import model.hibernate.PaymentEntity;
import model.error.AppError;

import java.util.List;

public interface DaoPayment {
    Either<AppError, List<PaymentEntity>> getAll();
}
