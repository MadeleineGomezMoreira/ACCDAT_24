package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    private int id;
    private String name;
    private LocalDate birthDate;
    private String phone;

    public Patient(int id) {
        this.id = id;
    }

    public Patient(String name) {
        this.name = name;
    }
    public Patient(List<String> string) {
        this.name = string.get(0);
    }

    @Override
    public String toString() {
        return "\n" + "-- Patient --" +
                "\n" + "| name: " + name +
                "\n" + "| birthday: " + birthDate +
                "\n" + "| phone: " + phone;
    }
}
