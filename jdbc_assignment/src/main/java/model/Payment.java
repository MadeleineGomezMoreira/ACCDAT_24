package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private int id;
    private double quantity;
    private LocalDate paymentDate;
    private int patientId;

    public Payment(int patientId) {
        this.patientId = patientId;
    }

    @Override
    public String toString() {
        return "\n" + "-- PAYMENT --" +
                "\n" + "| quantity: " + quantity +
                "\n" + "| payment date: " + paymentDate.toString() +
                "\n" + "| associated patient ID: " + patientId;
    }
}
