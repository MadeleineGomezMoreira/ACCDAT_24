package model.hibernate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prescribed_medication")
@NamedQueries({
        @NamedQuery(name = "DELETE_PRESCRIBED_MEDICATION_BY_PATIENT_ID_HQL",
                query = "delete from PrescribedMedicationEntity where medicalRecordId in (select m.id from MedicalRecordEntity m where m.patientId = :patientId)"),
})
public class PrescribedMedicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "dose")
    private String dose;
    @Column(name = "id_medical_record")
    private int medicalRecordId;

    public PrescribedMedicationEntity(int id, String name, String dose) {
        this.id = id;
        this.name = name;
        this.dose = dose;
    }

    @Override
    public String toString() {
        return "\n" + "--PrescribedMedicationEntity--" +
                "\n" + "Id: " + id +
                "\n" + "Name: " + name +
                "\n" + "Dose: " + dose +
                "\n" + "MedicalRecordId: " + medicalRecordId;
    }
}
