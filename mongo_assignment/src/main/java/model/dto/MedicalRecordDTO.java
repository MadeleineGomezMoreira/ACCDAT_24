package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordDTO {

    private LocalDate admissionDate;
    private String diagnosis;
    private ObjectId doctorId;

    @Override
    public String toString() {
        return "\n" + "--MEDICAL RECORD--" +
                "\n" + "Date of admission: " + admissionDate +
                "\n" + "Diagnosis: " + diagnosis +
                "\n" + "DoctorEntity id: " + doctorId;
    }
}
