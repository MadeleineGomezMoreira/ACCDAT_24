package dao.impl;

import dao.common.QueryStrings;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.Appointment;
import model.error.AppError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class DaoAppointmentImpl implements dao.DaoAppointment {

    private ResultSet rs;
    private PreparedStatement pStmt;
    private final DBConnectionPool pool;

    @Inject
    public DaoAppointmentImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    //get the appointment date shared by the most patients
    @Override
    public Either<AppError, Appointment> get() {
        Either<AppError, Appointment> result;
        try (Connection con = pool.getConnection()) {

            pStmt = con.prepareStatement(QueryStrings.GET_APPOINTMENT_DATE_SHARED_BY_MOST_PATIENTS);
            rs = pStmt.executeQuery();

            List<Appointment> appointments = readRS(rs);
            result = Either.right(appointments.get(0));

        } catch (SQLException e) {
            result = Either.left(new AppError(e.getMessage()));
        }
        return result;
    }

    private List<Appointment> readRS(ResultSet rs) {
        List<Appointment> listAppointments = new ArrayList<>();
        try {
            while (rs.next()) {
                LocalDate appointmentDate = rs.getDate("appointment_date").toLocalDate();
                listAppointments.add(new Appointment(appointmentDate));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return listAppointments;
    }
}
