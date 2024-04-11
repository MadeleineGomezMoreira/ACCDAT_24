package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {

    private String name;
    private LocalDate birthDate;
    private String phone;
    private double totalAmountPaid;

    @Override
    public String toString() {
        return "\n" + "-- PATIENT --" +
                "\n" + "| name: " + name +
                "\n" + "| birthday: " + birthDate +
                "\n" + "| phone: " + phone +
                "\n" + "| total € paid: " + totalAmountPaid;
    }

}
