package model;

import common.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescribedMedication {

    private int id;
    private String name;
    private String dose;
    private int medicalRecordId;

    public PrescribedMedication(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public PrescribedMedication(String fileLine) {
        String[] data = fileLine.split(Constants.SEMICOLON);
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.dose = data[2];
        this.medicalRecordId = Integer.parseInt(data[3]);
    }

    public String toStringTextFile() {
        return this.id + Constants.SEMICOLON +
                this.name + Constants.SEMICOLON +
                this.dose + Constants.SEMICOLON +
                this.medicalRecordId;
    }

    @Override
    public String toString() {
        return "\n" + "-- MEDICATION --" +
                "\n" + "Name: " + name +
                "\n" + "Dose: " + dose;
    }
}
