package dao.txt;

import io.vavr.control.Either;
import model.Credential;
import model.error.AppError;

public interface DaoLogin {
    Either<AppError, Credential> get();
}
