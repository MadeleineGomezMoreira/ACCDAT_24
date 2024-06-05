package model.hibernate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medical_records")
public class MedicalRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "admission_date")
    private LocalDate admissionDate;
    @Column(name = "diagnosis")
    private String diagnosis;
    @Column(name = "id_patient")
    private int patientId;
    @Column(name = "id_doctor")
    private int doctorId;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medical_record")
    private List<PrescribedMedicationEntity> prescribedMedication = new ArrayList<>();

    public MedicalRecordEntity(int patientId) {
        this.patientId = patientId;
    }

    public MedicalRecordEntity(int patientId, int doctorId) {
        this.patientId = patientId;
        this.doctorId = doctorId;
    }


    public MedicalRecordEntity(int id, LocalDate admissionDate, String diagnosis, int patientId, int doctorId) {
        this.id = id;
        this.admissionDate = admissionDate;
        this.diagnosis = diagnosis;
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    @Override
    public String toString() {
        if (prescribedMedication == null || prescribedMedication.isEmpty()) {
            return "\n" + "--MedicalRecordEntity--" +
                    "\n" + "Id: " + id +
                    "\n" + "AdmissionDate: " + admissionDate +
                    "\n" + "Diagnosis: " + diagnosis +
                    "\n" + "PatientId: " + patientId +
                    "\n" + "DoctorId: " + doctorId;
        } else {
            return "\n" + "--MedicalRecordEntity--" +
                    "\n" + "Id: " + id +
                    "\n" + "AdmissionDate: " + admissionDate +
                    "\n" + "Diagnosis: " + diagnosis +
                    "\n" + "PatientId: " + patientId +
                    "\n" + "DoctorId: " + doctorId +
                    "\n" + "PrescribedMedicationEntity: " + prescribedMedication;
        }
    }
}
