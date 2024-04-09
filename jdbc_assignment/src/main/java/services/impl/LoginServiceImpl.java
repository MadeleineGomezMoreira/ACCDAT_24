package services.impl;

import common.Constants;
import dao.DaoCredential;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Credential;
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
            Either<AppError, Credential> getCredential = daoCredential.get(new Credential(username));
            if (getCredential.isLeft()) {
                result = Either.left(new AppError(Constants.NO_CREDENTIAL_ASSOCIATED_TO_USERNAME_ERROR));
            } else {
                Credential credential = getCredential.get();
                String credentialPassword = credential.getPassword();
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

    @Override
    public boolean isAdmin(String username, String password) {
        if(username.equals(Constants.ADMIN_USER) && password.equals(Constants.ADMIN_PASS)) {
            return true;
        } else {
            return false;
        }
    }
}
