package dao.hibernate;

import io.vavr.control.Either;
import model.hibernate.CredentialEntity;
import model.error.AppError;

public interface DaoCredential {
    Either<AppError, CredentialEntity> get(CredentialEntity credentialEntity);

    Either<AppError, Integer> save(CredentialEntity credentialEntity);
}
