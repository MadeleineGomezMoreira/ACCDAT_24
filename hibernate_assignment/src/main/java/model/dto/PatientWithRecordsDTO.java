package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientWithRecordsDTO {
    private int patientId;
    private List<MedicalRecordDTO> medicalRecords = new ArrayList<>();

    @Override
    public String toString() {
        return "\n" + "--PATIENT--" +
                "\n" + "Patient Id: " + patientId +
                "\n" + "Medical records: " + medicalRecords;
    }
}
