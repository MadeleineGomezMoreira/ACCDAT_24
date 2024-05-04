package dao.mappers;

import model.MedicalRecord;
import model.dto.PatientMedicationAmountDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PatientMedicationAmountDTOMapper implements RowMapper<PatientMedicationAmountDTO> {

    @Override
    public PatientMedicationAmountDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PatientMedicationAmountDTO mr = new PatientMedicationAmountDTO();
        mr.setName(rs.getString("name"));
        mr.setMedicationAmount(rs.getInt("medication_amount"));
        return mr;
    }
}
