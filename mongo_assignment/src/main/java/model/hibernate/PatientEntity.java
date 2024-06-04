package model.hibernate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "patient")
    private CredentialEntity credential;

    public PatientEntity(int id, String name, LocalDate birthDate, String phone, CredentialEntity credential) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.credential = credential;
    }

    public PatientEntity(int id, String name, LocalDate birthDate, String phone) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
    }

    public PatientEntity(int id) {
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
