package model;

import common.Constants;
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

    public Patient(List<String> string) {
        this.name = string.get(0);
    }

    public Patient(String fileLine) {
        String[] data = fileLine.split(Constants.SEMICOLON);
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.birthDate = LocalDate.parse(data[2]);
        this.phone = data[3];
    }

    public String toStringTextFile() {
        return this.id + Constants.SEMICOLON +
                this.name + Constants.SEMICOLON +
                this.birthDate + Constants.SEMICOLON +
                this.phone;
    }

    @Override
    public String toString() {
        return "\n" + "-- Patient --" +
                "\n" + "| name: " + name +
                "\n" + "| birthday: " + birthDate +
                "\n" + "| phone: " + phone;
    }
}
