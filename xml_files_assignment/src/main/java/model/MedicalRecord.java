package model;

import common.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {

    private int id;
    private LocalDate admissionDate;
    private String diagnosis;
    private int patientId;
    private int doctorId;

    public MedicalRecord(int patientId) {
        this.patientId = patientId;
    }

    public MedicalRecord(String fileLine){
        String[] data = fileLine.split(Constants.SEMICOLON);
        this.id = Integer.parseInt(data[0]);
        this.admissionDate = LocalDate.parse(data[1]);
        this.diagnosis = data[2];
        this.patientId = Integer.parseInt(data[3]);
        this.doctorId = Integer.parseInt(data[4]);
    }

    public String toStringTextFile(){
        return this.id + Constants.SEMICOLON +
                this.admissionDate + Constants.SEMICOLON +
                this.diagnosis + Constants.SEMICOLON +
                this.patientId + Constants.SEMICOLON +
                this.doctorId;
    }

    @Override
    public String toString() {
        return "\n" + "-- MedicalRecord -- " +
                "\n" + "| admissionDate: " + admissionDate +
                "\n" + "| diagnosis: " + diagnosis +
                "\n" + "| patientId: " + patientId +
                "\n" + "| doctorId: " + doctorId;
    }
}
