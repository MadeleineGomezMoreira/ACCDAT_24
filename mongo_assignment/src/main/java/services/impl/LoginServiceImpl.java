package services.impl;

import common.Constants;
import dao.hibernate.DaoCredential;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.hibernate.CredentialEntity;
import model.error.AppError;
import services.LoginService;

public class LoginServiceImpl implements LoginService {

    private final DaoCredential daoCredential;

    @Inject
    public LoginServiceImpl(DaoCredential daoCredential) {
        this.daoCredential = daoCredential;
    }

    @Override
    public Either<AppError, Boolean> login(String username, String password) {
        Either<AppError, Boolean> result;
        try {
            Either<AppError, CredentialEntity> getCredential = daoCredential.get(new CredentialEntity(username));
            if (getCredential.isLeft()) {
                result = Either.left(new AppError(Constants.NO_CREDENTIAL_ASSOCIATED_TO_USERNAME_ERROR));
            } else {
                CredentialEntity credentialEntity = getCredential.get();
                String credentialPassword = credentialEntity.getPassword();
                if (password.equals(credentialPassword)) {
                    result = Either.right(true);
                } else {
                    result = Either.right(false);
                }
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        }
        return result;
    }

}
