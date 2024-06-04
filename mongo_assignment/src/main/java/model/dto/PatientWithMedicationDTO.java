package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientWithMedicationDTO {

    private ObjectId ObjectId;
    private List<PrescribedMedicationDTO> medication;

    @Override
    public String toString() {
        return "\n" + "--PATIENT--" +
                "\n" + "ID: " + ObjectId +
                "\n" + "Medication: " + medication;
    }
}
