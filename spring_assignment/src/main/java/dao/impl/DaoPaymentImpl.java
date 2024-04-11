package dao.impl;

import dao.common.QueryStrings;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.Payment;
import model.error.AppError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class DaoPaymentImpl implements dao.DaoPayment {
    private ResultSet rs;
    private PreparedStatement pStmt;
    private final DBConnectionPool pool;

    @Inject
    public DaoPaymentImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    //get all the payments done by a specific patient
    @Override
    public Either<AppError, List<Payment>> getAll(Payment payment) {
        try (Connection con = pool.getConnection()) {
            int patientId = payment.getPatientId();
            pStmt = con.prepareStatement(QueryStrings.GET_ALL_PAYMENTS_BY_PATIENT_ID);
            pStmt.setInt(1, patientId);
            rs = pStmt.executeQuery();
            return Either.right(readRS(rs));
        } catch (SQLException e) {
            return Either.left(new AppError(e.getMessage()));
        }
    }

    private ArrayList<Payment> readRS(ResultSet rs) {
        ArrayList<Payment> listPayment = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                double quantity = rs.getDouble("quantity");
                LocalDate paymentDate = rs.getDate("payment_date").toLocalDate();
                int patientId = rs.getInt("id_patient");
                listPayment.add(new Payment(id, quantity, paymentDate, patientId));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return listPayment;
    }
}
