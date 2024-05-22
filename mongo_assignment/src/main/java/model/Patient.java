package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "patients")
@NamedQueries({
        @NamedQuery(name = "HQL_GET_ALL_PATIENTS",
                query = "from Patient"),
        @NamedQuery(name = "HQL_GET_PATIENT_BY_ID",
                query = "from Patient where id=:id"),
        @NamedQuery(name = "HQL_INSERT_PATIENT",
                query = "insert into Patient (id, name, birthDate, phone) select (:id, :name, :birthDate, :phone)"),
        @NamedQuery(name = "HQL_UPDATE_PATIENT",
                query = "update Patient set name = :name, birthDate = :birthDate, phone = :phone where id = :id"),
        @NamedQuery(name = "HQL_DELETE_PATIENT_BY_ID",
                query = "delete from Patient where id=:id"),
        @NamedQuery(name = "GET_THE_NAME_AND_NUMBER_OF_PRESCRIBED_MEDICINES_OF_EACH_PATIENT_HQL",
                query = "select p.name, count(pm.id) from MedicalRecord mr join Patient p on mr.patientId = p.id join mr.prescribedMedication pm group by p.id, p.name"),
})
public class Patient {

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

    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "patient")
    private Credential credential;

    public Patient(int id, String name, LocalDate birthDate, String phone, Credential credential) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.credential = credential;
    }

    public Patient(int id, String name, LocalDate birthDate, String phone) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
    }

    public Patient(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "\n" + "--PATIENT--" +
                "\n" + "Id: " + id +
                "\n" + "Name: " + name +
                "\n" + "Birthday: " + birthDate +
                "\n" + "Phone: " + phone;
    }
}
