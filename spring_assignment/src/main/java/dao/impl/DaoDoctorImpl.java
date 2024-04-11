package dao.impl;

import dao.common.QueryStrings;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.Credential;
import model.Doctor;
import model.error.AppError;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Log4j2
public class DaoDoctorImpl implements dao.DaoDoctor {

    private final DBConnectionPool pool;

    @Inject
    public DaoDoctorImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Either<AppError, Doctor> get(Doctor doctor) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            int doctorId = doctor.getId();

            List<Doctor> doctors = jdbcTemplate.query(QueryStrings.GET_DOCTOR_BY_ID, BeanPropertyRowMapper.newInstance(Doctor.class), doctorId);
            return Either.right(doctors.get(0));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Either.left(new AppError(e.getMessage()));
        }
    }

}
