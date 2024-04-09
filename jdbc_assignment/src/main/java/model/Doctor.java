package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    private int id;
    private String name;
    private String speciality;
    private String phone;

    public Doctor(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "\n" + "-- DOCTOR -- " +
                "\n" + "| name: " + name +
                "\n" + "| specialty: " + speciality +
                "\n" + "| phone: " + phone;
    }

}
