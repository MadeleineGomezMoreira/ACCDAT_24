package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.mongo.MedicalRecord;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientWithRecordsDTO {
    private ObjectId patientId;
    private List<MedicalRecordDTO> medicalRecords = new ArrayList<>();

    @Override
    public String toString() {
        return "\n" + "--PATIENT--" +
                "\n" + "PatientEntity Id: " + patientId +
                "\n" + "Medical records: " + medicalRecords;
    }
}
