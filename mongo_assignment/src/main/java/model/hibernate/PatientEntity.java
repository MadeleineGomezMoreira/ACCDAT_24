package model.hibernate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "patients")
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "date_of_birth")
    private LocalDate birthDate;
    @Column(name = "phone")
    private String phone;
    //I will set this to eager just to retrieve all the data from the database
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_patient")
    private List<MedicalRecordEntity> medicalRecords;

    @Override
    public String toString() {
        return "\n" + "--PATIENT--" +
                "\n" + "Id: " + id +
                "\n" + "Name: " + name +
                "\n" + "Birthday: " + birthDate +
                "\n" + "Phone: " + phone;
    }
}
