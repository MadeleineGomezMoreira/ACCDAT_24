package dao.impl;

import common.Constants;
import dao.common.QueryStrings;
import dao.connection.DBConnectionPool;
import dao.mappers.PrescribedMedicationMapper;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.PrescribedMedication;
import model.error.AppError;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class DaoPrescribedMedicationImpl implements dao.DaoPrescribedMedication {
    private final DBConnectionPool pool;

    @Inject
    public DaoPrescribedMedicationImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    //get old prescribed medication (older than 1 year)
    @Override
    public Either<AppError, List<PrescribedMedication>> getAll(PrescribedMedication prescribedMedication) {
        Either<AppError, List<PrescribedMedication>> result;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            List<PrescribedMedication> listPrescribedMedication = jdbcTemplate.query(QueryStrings.GET_ALL_OLD_PRESCRIBED_MEDICATIONS, new PrescribedMedicationMapper());
            if (listPrescribedMedication.isEmpty()) {
                return Either.left(new AppError(Constants.NO_MEDICATION_PRESCRIBED_TO_PATIENT_ERROR));
            } else {
                result = Either.right(listPrescribedMedication);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Either.left(new AppError(e.getMessage()));
        }
        return result;
    }

    @Override
    //get all prescribed medication
    public Either<AppError, List<PrescribedMedication>> getAll() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            List<PrescribedMedication> medication = jdbcTemplate.query(QueryStrings.GET_ALL_MEDICATION, new PrescribedMedicationMapper());
            return Either.right(medication);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Either.left(new AppError(e.getMessage()));
        }
    }

    @Override
    //update prescribed-medication dose by id
    public Either<AppError, Integer> update(PrescribedMedication prescribedMedication) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            return Either.right(jdbcTemplate.update(QueryStrings.UPDATE_PRESCRIBED_MEDICATION_DOSE_BY_ID, prescribedMedication.getDose(), prescribedMedication.getId()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Either.left(new AppError(e.getMessage()));
        }
    }

    @Override
    //save new prescribed-medication to the newest medical record of a specific patient
    //SPRING
    public Either<AppError, Integer> save(PrescribedMedication prescribedMedication) {
        Either<AppError, Integer> result;
        try {
            Map<String, Object> params = new HashMap<>();
            SimpleJdbcInsert insert = new SimpleJdbcInsert(pool.getDataSource())
                    .withTableName("prescribed_medication")
                    .usingColumns("name", "dose", "id_medical_record")
                    .usingGeneratedKeyColumns("id");

            params.put("name", prescribedMedication.getName());
            params.put("dose", prescribedMedication.getDose());
            params.put("id_medical_record", prescribedMedication.getMedicalRecordId());

            int affectedRows = insert.execute(params);

            if (affectedRows == 0) {
                result = Either.left(new AppError(Constants.MEDICATION_INSERTION_DB_ERROR));
            } else {
                result = Either.right(affectedRows);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Either.left(new AppError(e.getMessage()));
        }
        return result;
    }
}
