package dao.mappers;

import model.MedicalRecord;
import model.PrescribedMedication;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrescribedMedicationMapper implements RowMapper<PrescribedMedication> {

    @Override
    public PrescribedMedication mapRow(ResultSet rs, int rowNum) throws SQLException {
        PrescribedMedication pm = new PrescribedMedication();
        pm.setId(rs.getInt("id"));
        pm.setName(rs.getString("name"));
        pm.setDose(rs.getString("dose"));
        pm.setMedicalRecordId(rs.getInt("id_medical_record"));
        return pm;
    }
}
