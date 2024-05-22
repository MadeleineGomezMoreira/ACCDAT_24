package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordDTO {

    private int id;
    private LocalDate admissionDate;
    private String diagnosis;
    private int doctorId;

    @Override
    public String toString() {
        return "\n" + "--MEDICAL RECORD--" +
                "\n" + "Id: " + id +
                "\n" + "Date of admission: " + admissionDate +
                "\n" + "Diagnosis: " + diagnosis +
                "\n" + "Doctor id: " + doctorId;
    }
}
