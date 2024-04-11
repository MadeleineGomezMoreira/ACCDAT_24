package dao.impl;

import dao.common.QueryStrings;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.Credential;
import model.error.AppError;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Log4j2
public class DaoCredentialImpl implements dao.DaoCredential {
    private final DBConnectionPool pool;

    @Inject
    public DaoCredentialImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Either<AppError, Credential> get(Credential credential) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            String username = credential.getUsername();

            List<Credential> credentials = jdbcTemplate.query(QueryStrings.GET_CREDENTIAL_BY_USERNAME, BeanPropertyRowMapper.newInstance(Credential.class), username);
            return Either.right(credentials.get(0));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Either.left(new AppError(e.getMessage()));
        }
    }
}
