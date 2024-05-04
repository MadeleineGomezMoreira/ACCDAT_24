package dao.impl;

import common.Constants;
import dao.common.QueryStrings;
import dao.connection.DBConnectionPool;
import dao.mappers.MedicalRecordMapper;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.MedicalRecord;
import model.PrescribedMedication;
import model.error.AppError;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
public class DaoMedicalRecordImpl implements dao.DaoMedicalRecord {

    private final DBConnectionPool pool;

    @Inject
    public DaoMedicalRecordImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    //get all the medical records
    //SPRING
    public Either<AppError, List<MedicalRecord>> getAll() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            List<MedicalRecord> medicalRecords = jdbcTemplate.query(QueryStrings.GET_ALL_MEDICAL_RECORDS, new MedicalRecordMapper());
            return Either.right(medicalRecords);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Either.left(new AppError(e.getMessage()));
        }
    }

    @Override
    //get the newest medical record of a specific patient
    //SPRING
    public Either<AppError, MedicalRecord> get(MedicalRecord medicalRecord) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            int patientId = medicalRecord.getPatientId();
            List<MedicalRecord> medicalRecords = jdbcTemplate.query(QueryStrings.GET_NEWEST_MEDICAL_RECORD_BY_PATIENT_ID, new MedicalRecordMapper(), patientId);
            if (medicalRecords.isEmpty()) {
                return Either.left(new AppError(Constants.NO_MEDICAL_RECORDS_FOUND_FOR_PATIENT_ERROR));
            }
            return Either.right(medicalRecords.get(0));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Either.left(new AppError(e.getMessage()));
        }
    }

    @Override
    //transactional --> delete medical records older than 1 year from the DB (medication too)
    //SPRING
    public Either<AppError, Integer> delete() {
        Either<AppError, Integer> result;

        TransactionDefinition def = new DefaultTransactionDefinition();
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(pool.getDataSource());
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(transactionManager.getDataSource()));

            jdbcTemplate.update(QueryStrings.DELETE_PRESCRIBED_MEDICATION_FROM_OLD_MEDICAL_RECORDS);
            result = Either.right(jdbcTemplate.update(QueryStrings.DELETE_OLD_MEDICAL_RECORDS));
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            result = Either.left(new AppError(e.getMessage()));
        }
        return result;
    }
}
