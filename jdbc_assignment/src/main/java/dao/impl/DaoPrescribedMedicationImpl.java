package dao.impl;

import common.Constants;
import dao.common.QueryStrings;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.PrescribedMedication;
import model.error.AppError;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class DaoPrescribedMedicationImpl implements dao.DaoPrescribedMedication {

    private ResultSet rs;
    private PreparedStatement pStmt;
    private final DBConnectionPool pool;

    @Inject
    public DaoPrescribedMedicationImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Either<AppError, List<PrescribedMedication>> getAll(PrescribedMedication prescribedMedication) {
        Either<AppError, List<PrescribedMedication>> result;
        try {
            int patientId = prescribedMedication.getMedicalRecordId();
            pStmt = pool.getConnection().prepareStatement(QueryStrings.SELECT_PRESCRIBED_MEDICATION_BY_PATIENT_ID);
            pStmt.setInt(1, patientId);
            rs = pStmt.executeQuery();

            List<PrescribedMedication> listPrescribedMedication = readRS(rs);

            if (listPrescribedMedication.isEmpty()) {
                return Either.left(new AppError(Constants.NO_MEDICATION_PRESCRIBED_TO_PATIENT_ERROR));
            } else {
                result = Either.right(listPrescribedMedication);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            result = Either.left(new AppError(e.getMessage()));
        }
        return result;
    }

    private ArrayList<PrescribedMedication> readRS(ResultSet rs) {
        ArrayList<PrescribedMedication> listPrescribedMedication = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String dose = rs.getString("dose");
                int medicalRecordId = rs.getInt("id_medical_record");
                listPrescribedMedication.add(new PrescribedMedication(id, name, dose, medicalRecordId));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return listPrescribedMedication;
    }
}
