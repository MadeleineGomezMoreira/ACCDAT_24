package dao.impl;

import dao.DaoQueries;
import dao.common.QueryStrings;
import dao.connection.DBConnectionPool;
import dao.mappers.PatientMedicationAmountDTOMapper;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.dto.PatientMedicationAmountDTO;
import model.error.AppError;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.List;

@Log4j2
public class DaoQueriesImpl implements DaoQueries {

    private final DBConnectionPool pool;

    @Inject
    public DaoQueriesImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Either<AppError,List<String>> getQueryOne() {
        try{
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());

            //mapping a single column (https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/SingleColumnRowMapper.html)
            List<String> patientNames = jdbcTemplate.query(QueryStrings.GET_NAMES_OF_PATIENTS_MEDICATED_WITH_400_MG_IBUPROFEN, new SingleColumnRowMapper<>(String.class));
            return Either.right(patientNames);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return Either.left(new AppError(e.getMessage()));
        }
    }

    @Override
    public Either<AppError, List<PatientMedicationAmountDTO>> getQueryTwo() {
        try{
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());

            List<PatientMedicationAmountDTO> patientMedicationAmountDTO = jdbcTemplate.query(QueryStrings.GET_NAME_AND_TOTAL_PRESCRIBED_MEDICATIONS_OF_PATIENT, new PatientMedicationAmountDTOMapper());
            return Either.right(patientMedicationAmountDTO);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return Either.left(new AppError(e.getMessage()));
        }
    }
}
