package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientWithMedNumber {

    private String name;
    private Long medicationNumber;

    @Override
    public String toString() {
        return "\n" + "--PATIENT--" +
                "\n" + "Name: " + name +
                "\n" + "Nº of prescribed medicines: " + medicationNumber + "\n";
    }
}
