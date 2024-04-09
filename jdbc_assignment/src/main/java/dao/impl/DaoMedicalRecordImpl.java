package dao.impl;

import dao.common.QueryStrings;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.MedicalRecord;
import model.PrescribedMedication;
import model.error.AppError;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class DaoMedicalRecordImpl implements dao.DaoMedicalRecord {

    private ResultSet rs;
    private PreparedStatement pStmt;
    private final DBConnectionPool pool;

    @Inject
    public DaoMedicalRecordImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Either<AppError, List<MedicalRecord>> getAll(MedicalRecord medicalRecord) {
        try (Connection con = pool.getConnection()) {
            int patientId = medicalRecord.getPatientId();
            pStmt = con.prepareStatement(QueryStrings.GET_ALL_MEDICAL_RECORDS_BY_PATIENT_ID);
            pStmt.setInt(1, patientId);

            rs = pStmt.executeQuery();
            return Either.right(readRS(rs));

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Either.left(new AppError(e.getMessage()));
        }
    }

    @Override
    public Either<AppError, Integer> save(MedicalRecord medicalRecord) {
        try (Connection con = pool.getConnection()) {
            con.setAutoCommit(false);
            List<PrescribedMedication> medication = medicalRecord.getPrescribedMedication();

            //save the medical record
            pStmt = con.prepareStatement(QueryStrings.INSERT_MEDICAL_RECORD, Statement.RETURN_GENERATED_KEYS);
            pStmt.setDate(1, Date.valueOf(medicalRecord.getAdmissionDate()));
            pStmt.setString(2, medicalRecord.getDiagnosis());
            pStmt.setInt(3, medicalRecord.getPatientId());
            pStmt.setInt(4, medicalRecord.getDoctorId());
            pStmt.executeUpdate();

            rs = pStmt.getGeneratedKeys();
            int generatedId = readKeyRS(rs);

            //save the prescribed medication
            for (PrescribedMedication prescribedMedication : medication) {
                pStmt = con.prepareStatement(QueryStrings.INSERT_PRESCRIBED_MEDICATION);
                pStmt.setString(1, prescribedMedication.getName());
                pStmt.setString(2, prescribedMedication.getDose());
                pStmt.setInt(3, generatedId);
                pStmt.executeUpdate();
            }
            con.commit();
            con.setAutoCommit(true);
            return Either.right(0);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Either.left(new AppError(e.getMessage()));
        }
    }

    private List<MedicalRecord> readRS(ResultSet rs) {
        ArrayList<MedicalRecord> listMedicalRecords = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDate admissionDate = rs.getDate("admission_date").toLocalDate();
                String diagnosis = rs.getString("diagnosis");
                int patientId = rs.getInt("id_patient");
                int doctorId = rs.getInt("id_doctor");
                listMedicalRecords.add(new MedicalRecord(id, admissionDate, diagnosis, patientId, doctorId));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return listMedicalRecords;
    }

    private int readKeyRS(ResultSet rs) {
        int key = 0;
        try {
            while (rs.next()) {
                key = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return key;
    }


}
