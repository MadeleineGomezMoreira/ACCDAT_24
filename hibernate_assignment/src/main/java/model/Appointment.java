package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "appointment_date")
    private String appointmentDate;
    @Column(name = "id_patient")
    private int patientId;
    @Column(name = "id_doctor")
    private int doctorId;
}
