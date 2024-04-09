package dao.impl;

import dao.common.QueryStrings;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.Credential;
import model.error.AppError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class DaoCredentialImpl implements dao.DaoCredential {

    private ResultSet rs;
    private PreparedStatement pStmt;
    private final DBConnectionPool pool;

    @Inject
    public DaoCredentialImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Either<AppError, Credential> get(Credential credential) {
        try (Connection con = pool.getConnection()) {

            String username = credential.getUsername();

            pStmt = con.prepareStatement(QueryStrings.GET_CREDENTIAL_BY_USERNAME);
            pStmt.setString(1, username);

            rs = pStmt.executeQuery();
            List<Credential> credentials = readRS(rs);
            return Either.right(credentials.get(0));

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Either.left(new AppError(e.getMessage()));
        }
    }

    private List<Credential> readRS(ResultSet rs) {
        List<Credential> listCredentials = new ArrayList<>();
        try {
            while (rs.next()) {
                int patientId = 0;
                int id = rs.getInt("id");
                String userName = rs.getString("username");
                String password = rs.getString("password");
                if (!userName.equals("root") && !password.equals("2dam")) {
                    patientId = rs.getInt("id_patient");
                }
                listCredentials.add(new Credential(id, userName, password, patientId));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return listCredentials;
    }
}
