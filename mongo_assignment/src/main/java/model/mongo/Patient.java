package model.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {

    @SerializedName("_id")
    private ObjectId patientId;
    @SerializedName("name")
    private String name;
    @SerializedName("date_of_birth")
    private LocalDate birthDate;
    @SerializedName("phone")
    private String phone;
    @SerializedName("medical_records")
    private List<MedicalRecord> medicalRecords;

    @Override
    public String toString() {
        if(medicalRecords != null) {
            return "\n" + "--PATIENT--" +
                    "\n" + "ID: " + patientId +
                    "\n" + "Name: " + name +
                    "\n" + "Birthday: " + birthDate +
                    "\n" + "Phone: " + phone
                    + "\n" + "Medical Records: " + medicalRecords;
        } else {
            return "\n" + "--PATIENT--" +
                    "\n" + "ID: " + patientId +
                    "\n" + "Name: " + name +
                    "\n" + "Birthday: " + birthDate +
                    "\n" + "Phone: " + phone;
        }
    }

}
