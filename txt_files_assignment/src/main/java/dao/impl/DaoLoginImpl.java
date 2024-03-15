package dao.impl;

import common.Constants;
import common.config.LoginConfiguration;
import dao.DaoLogin;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Credential;
import model.error.AppError;

public class DaoLoginImpl implements DaoLogin {

    private final LoginConfiguration config;

    @Inject
    public DaoLoginImpl(LoginConfiguration config) {
        this.config = config;
    }

    @Override
    public Either<AppError, Credential> get() {
        Either<AppError, Credential> result;

        String username = config.getUsername();
        String password = config.getPassword();

        if (username == null || password == null) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
        } else {
            result = Either.right(new Credential(username, password));
        }

        return result;
    }

}
