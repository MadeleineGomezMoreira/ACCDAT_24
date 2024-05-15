package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "medical_records")
@NamedQueries({
        @NamedQuery(name = "DELETE_MEDICAL_RECORDS_BY_PATIENT_ID_HQL",
                query = "delete from MedicalRecord where patientId = :id"),
        @NamedQuery(name = "GET_ADMISSION_DATE_WITH_MOST_PATIENTS_HQL",
                query = "select admissionDate from MedicalRecord group by admissionDate order by count(admissionDate) desc limit 1"),
        @NamedQuery(name = "GET_PATIENT_ID_WITH_MOST_MEDICAL_RECORDS_HQL",
                query = "select patientId from MedicalRecord group by patientId order by count(patientId) desc limit 1"),
})
public class MedicalRecord {

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
    @OneToMany(mappedBy = "medicalRecordId", cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<PrescribedMedication> prescribedMedication = new ArrayList<>();

    public MedicalRecord(int patientId) {
        this.patientId = patientId;
    }

    public MedicalRecord(int patientId, int doctorId) {
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    @Override
    public String toString() {
        if (prescribedMedication == null || prescribedMedication.isEmpty()) {
            return "\n" + "--MedicalRecord--" +
                    "\n" + "Id: " + id +
                    "\n" + "AdmissionDate: " + admissionDate +
                    "\n" + "Diagnosis: " + diagnosis +
                    "\n" + "PatientId: " + patientId +
                    "\n" + "DoctorId: " + doctorId;
        } else {
            return "\n" + "--MedicalRecord--" +
                    "\n" + "Id: " + id +
                    "\n" + "AdmissionDate: " + admissionDate +
                    "\n" + "Diagnosis: " + diagnosis +
                    "\n" + "PatientId: " + patientId +
                    "\n" + "DoctorId: " + doctorId +
                    "\n" + "PrescribedMedication: " + prescribedMedication;
        }
    }
}
