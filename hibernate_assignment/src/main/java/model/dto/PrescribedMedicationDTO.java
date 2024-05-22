package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescribedMedicationDTO {
    private String name;
    private String dosage;

    @Override
    public String toString() {
        return "\n" + "--MEDICATION--" +
                "\n" + "Name: " + name +
                "\n" + "Dosage: " + dosage;
    }
}
