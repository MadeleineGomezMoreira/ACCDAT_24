package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {

    private int id;
    private LocalDate admissionDate;
    private String diagnosis;
    private int patientId;
    private int doctorId;
    private List<PrescribedMedication> prescribedMedication;

    public MedicalRecord(int id, LocalDate admissionDate, String diagnosis, int patientId, int doctorId) {
        this.id = id;
        this.admissionDate = admissionDate;
        this.diagnosis = diagnosis;
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    public MedicalRecord(int patientId) {
        this.patientId = patientId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (prescribedMedication != null && !prescribedMedication.isEmpty()) {
            builder.append("\n" + "-- MEDICAL RECORD -- " +
                    "\n" + "| admissionDate: " + admissionDate +
                    "\n" + "| diagnosis: " + diagnosis +
                    "\n" + "| patientId: " + patientId +
                    "\n" + "| doctorId: " + doctorId +
                    "\n" + "| prescribed medication:");
            for (PrescribedMedication medication : prescribedMedication) {
                builder.append("\n" + "|    MEDICATION:" + "\n" + "|   - Name: " + medication.getName() +
                        "\n" + "|   - Dose: " + medication.getDose());
            }
        } else {
            builder.append("\n" + "-- MEDICAL RECORD -- " +
                    "\n" + "| admissionDate: " + admissionDate +
                    "\n" + "| diagnosis: " + diagnosis +
                    "\n" + "| patientId: " + patientId +
                    "\n" + "| doctorId: " + doctorId);
        }
        return builder.toString();
    }

}
