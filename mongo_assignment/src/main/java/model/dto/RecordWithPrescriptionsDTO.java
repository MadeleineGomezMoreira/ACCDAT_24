package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordWithPrescriptionsDTO {

    private int recordId;
    private List<PrescribedMedicationDTO> prescription = new ArrayList<>();

    @Override
    public String toString() {
        return "\n" + "--MEDICAL RECORD--" +
                "\n" + "ID: " + recordId +
                "\n" + "Prescription: " + prescription;
    }

}
