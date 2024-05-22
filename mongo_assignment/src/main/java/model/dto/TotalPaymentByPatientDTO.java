package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalPaymentByPatientDTO {

    private int patientId;
    private double totalAmountPaid = 0;

    @Override
    public String toString() {
        return "\n" + "--PATIENT--" +
                "\n" + "ID: " + patientId +
                "\n" + "Total paid: " + totalAmountPaid + "\n";
    }

}
