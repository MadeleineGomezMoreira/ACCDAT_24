package services;

import io.vavr.control.Either;
import model.Credential;
import model.error.AppError;

public interface LoginService {
    Either<AppError, Boolean> login(String username, String password);
}
