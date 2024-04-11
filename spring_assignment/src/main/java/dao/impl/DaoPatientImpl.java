package dao.impl;

import common.Constants;
import dao.common.QueryStrings;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.Patient;
import model.error.AppError;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class DaoPatientImpl implements dao.DaoPatient {

    private ResultSet rs;
    private PreparedStatement pStmt;
    private final DBConnectionPool pool;

    @Inject
    public DaoPatientImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Either<AppError, List<Patient>> getAll() {
        try (Connection con = pool.getConnection()) {

            pStmt = con.prepareStatement(QueryStrings.GET_ALL_PATIENTS);
            rs = pStmt.executeQuery();
            return Either.right(readRS(rs));
        } catch (SQLException e) {
            return Either.left(new AppError(e.getMessage()));
        }
    }

    //get patient by id
    @Override
    public Either<AppError, List<Patient>> get(Patient patient) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            int patientId = patient.getId();

            List<Patient> patients = jdbcTemplate.query(QueryStrings.GET_PATIENT_BY_ID, BeanPropertyRowMapper.newInstance(Patient.class), patientId);
            return Either.right(patients);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Either.left(new AppError(e.getMessage()));
        }
    }

    //get patient by username
    @Override
    public Either<AppError, Patient> get(String username) {
        Either<AppError, Patient> result;
        try (Connection con = pool.getConnection()) {
            pStmt = con.prepareStatement(QueryStrings.GET_PATIENT_BY_USERNAME);
            pStmt.setString(1, username);
            rs = pStmt.executeQuery();

            List<Patient> patients = readRS(rs);

            if (patients.isEmpty()) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                result = Either.right(patients.get(0));
            }
        } catch (SQLException e) {
            result = Either.left(new AppError(e.getMessage()));
        }
        return result;
    }

    //get all by medication
    @Override
    public Either<AppError, List<Patient>> getAll(Patient patient) {
        Either<AppError, List<Patient>> result;
        try (Connection con = pool.getConnection()) {
            String medicationName = patient.getName();
            pStmt = con.prepareStatement(QueryStrings.GET_ALL_PATIENTS_BY_PRESCRIBED_MEDICATION_NAME);
            pStmt.setString(1, medicationName);
            rs = pStmt.executeQuery();

            List<Patient> patients = readRS(rs);

            if (patients.isEmpty()) {
                result = Either.left(new AppError(Constants.NO_PATIENTS_WITH_MEDICATION_ERROR));
            } else {
                result = Either.right(patients);
            }
        } catch (SQLException e) {
            result = Either.left(new AppError(e.getMessage()));
        }
        return result;
    }

    //get the patient with the most medical records
    @Override
    public Either<AppError, Patient> get() {
        Either<AppError, Patient> result;
        try (Connection con = pool.getConnection()) {
            pStmt = con.prepareStatement(QueryStrings.GET_PATIENT_WITH_THE_HIGHEST_NUMBER_OF_RECORDS);
            rs = pStmt.executeQuery();

            List<Patient> patients = readRS(rs);

            if (patients.isEmpty()) {
                result = Either.left(new AppError(Constants.NO_PATIENTS_WITH_MEDICATION_ERROR));
            } else {
                result = Either.right(patients.get(0));
            }
        } catch (SQLException e) {
            result = Either.left(new AppError(e.getMessage()));
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> delete(Patient patient, boolean confirmed) {
        Either<AppError, Integer> result;
        try (Connection con = pool.getConnection()) {
            int patientId = patient.getId();
            con.setAutoCommit(false);

            if (confirmed) {
                //delete prescribed medication
                pStmt = con.prepareStatement(QueryStrings.DELETE_PRESCRIBED_MEDICATION_BY_PATIENT_ID);
                pStmt.setInt(1, patientId);
                pStmt.executeUpdate();
            }

            //delete medical records (if the patient has prescribed medication, it will not be deleted)
            pStmt = con.prepareStatement(QueryStrings.DELETE_MEDICAL_RECORDS_BY_PATIENT_ID);
            pStmt.setInt(1, patientId);
            pStmt.executeUpdate();

            //delete payments
            pStmt = con.prepareStatement(QueryStrings.DELETE_PAYMENTS_BY_PATIENT_ID);
            pStmt.setInt(1, patientId);
            pStmt.executeUpdate();

            //delete appointments
            pStmt = con.prepareStatement(QueryStrings.DELETE_APPOINTMENTS_BY_PATIENT_ID);
            pStmt.setInt(1, patientId);
            pStmt.executeUpdate();

            //delete credential
            pStmt = con.prepareStatement(QueryStrings.DELETE_CREDENTIAL_BY_PATIENT_ID);
            pStmt.setInt(1, patientId);
            pStmt.executeUpdate();

            //no matter if they were deleted or not (they may not exist), I delete the patient
            pStmt = con.prepareStatement(QueryStrings.DELETE_PATIENT_BY_ID);
            pStmt.setInt(1, patientId);
            pStmt.executeUpdate();

            con.commit();
            con.setAutoCommit(true);

            result = Either.right(0);

        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                result = Either.left(new AppError(Constants.PATIENT_HAS_MEDICATION_ASSOCIATED_TO_MEDICAL_RECORDS_ERROR));
            } else {
                result = Either.left(new AppError(e.getMessage()));
            }
        }
        return result;
    }

    private ArrayList<Patient> readRS(ResultSet rs) {
        ArrayList<Patient> listPatients = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                String phone = rs.getString("phone");
                listPatients.add(new Patient(id, name, dateOfBirth, phone));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return listPatients;
    }
}
