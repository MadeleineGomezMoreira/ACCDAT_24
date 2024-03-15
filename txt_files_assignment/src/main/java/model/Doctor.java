package model;

import common.Constants;
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

    public Doctor(String fileLine) {
        String[] data = fileLine.split(Constants.SEMICOLON);
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.speciality = data[2];
        this.phone = data[3];
    }

    public String toStringTextFile() {
        return this.id + Constants.SEMICOLON +
                this.name + Constants.SEMICOLON +
                this.speciality + Constants.SEMICOLON +
                this.phone;
    }
}
