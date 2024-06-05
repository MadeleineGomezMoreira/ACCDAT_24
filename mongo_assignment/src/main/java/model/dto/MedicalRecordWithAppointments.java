package model.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordWithAppointments {

    @SerializedName("name")
    private String patientName;
    @SerializedName("number_of_appointments")
    private int appointmentNumber;
    @SerializedName("admission_date")
    private LocalDate admissionDate;
    @SerializedName("diagnosis")
    private String diagnosis;
    @SerializedName("doctor_id")
    private ObjectId doctorId;

    @Override
    public String toString() {
        return "\n" + "--MEDICAL RECORD--" +
                "\n" + "Patient name: " + patientName +
                "\n" + "Number of appointments: " + appointmentNumber +
                "\n" + "Date of admission: " + admissionDate +
                "\n" + "Diagnosis: " + diagnosis +
                "\n" + "Doctor ID: " + doctorId;
    }
}
