package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientMedicationAmountDTO {

    private String name;
    private int medicationAmount;

    @Override
    public String toString() {
        return "\n" + "-- PATIENT --" +
                "\n" + "name: " + name +
                "\n" + "total prescriptions: " + medicationAmount;
    }
}
