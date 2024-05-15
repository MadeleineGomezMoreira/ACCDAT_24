package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "quantity")
    private double quantity;
    @Column(name = "payment_date")
    private String paymentDate;
    @Column(name = "id_patient")
    private int patientId;
}
