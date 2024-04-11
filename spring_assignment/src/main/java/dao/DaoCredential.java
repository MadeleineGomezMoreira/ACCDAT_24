package dao;

import io.vavr.control.Either;
import model.Credential;
import model.error.AppError;

public interface DaoCredential {
    Either<AppError, Credential> get(Credential credential);
}
