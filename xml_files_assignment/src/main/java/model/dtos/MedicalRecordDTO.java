package model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.PrescribedMedication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordDTO {

    private int id;
    private LocalDate admissionDate;
    private String diagnosis;
    private int patientId;
    private int doctorId;
    private List<PrescribedMedication> medicationList = new ArrayList<>();

    @Override
    public String toString() {
        return "\n" + "-- MEDICAL RECORD --" +
                "\n" + "Admission date: " + admissionDate +
                "\n" + "Diagnosis: " + diagnosis +
                "\n" + "Patient id: " + patientId +
                "\n" + "Doctor id: " + doctorId +
                "\n" + medicationList;
    }
}
