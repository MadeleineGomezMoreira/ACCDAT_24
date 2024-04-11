package model;

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

    public PrescribedMedication(String dose) {
        this.dose = dose;
    }

    public PrescribedMedication(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public PrescribedMedication(String name, String dose) {
        this.name = name;
        this.dose = dose;
    }

    public PrescribedMedication(int id, String dose) {
        this.id = id;
        this.dose = dose;
    }

    @Override
    public String toString() {
        return "\n" + "-- MEDICATION --" +
                "\n" + "ID: " + id +
                "\n" + "Name: " + name +
                "\n" + "Dose: " + dose;
    }
}
