package services.impl;

import dao.txt.DaoLogin;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Credential;
import model.error.AppError;

public class LoginServiceImpl implements services.LoginService {

    private final DaoLogin daoLogin;

    @Inject
    public LoginServiceImpl(DaoLogin daoLogin) {
        this.daoLogin = daoLogin;
    }

    @Override
    public Either<AppError, Boolean> login(String username, String password) {
        Either<AppError, Boolean> result;
        Either<AppError, Credential> loginCredential = daoLogin.get();
        Credential credential = new Credential(username, password);

        if (loginCredential.isRight()) {
            result = Either.right(loginCredential.get().equals(credential));
        } else {
            result = Either.left(loginCredential.getLeft());
        }
        return result;
    }
}
