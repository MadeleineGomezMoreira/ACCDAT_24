package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    private int id;
    private LocalDate appointmentDate;
    private int patientId;
    private int doctorId;

    public Appointment(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    @Override
    public String toString() {
        return "\n" + "-- APPOINTMENT -- " +
                "\n" + "| appointmentDate: " + appointmentDate.toString() +
                "\n" + "| patient-ID: " + patientId +
                "\n" + "| doctor-ID: " + doctorId;
    }

}
