package dao.mappers;

import model.MedicalRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MedicalRecordMapper implements RowMapper<MedicalRecord> {

    @Override
    public MedicalRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        MedicalRecord mr = new MedicalRecord();
        mr.setId(rs.getInt("id"));
        mr.setAdmissionDate(rs.getDate("admission_date").toLocalDate());
        mr.setDiagnosis(rs.getString("diagnosis"));
        mr.setPatientId(rs.getInt("id_patient"));
        mr.setDoctorId(rs.getInt("id_doctor"));
        mr.setPrescribedMedication(new ArrayList<>());
        return mr;
    }
}
