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
public class MedicalRecord {

    @SerializedName("admission_date")
    private LocalDate admissionDate;
    @SerializedName("diagnosis")
    private String diagnosis;
    @SerializedName("doctor_id")
    private ObjectId doctorId;
    @SerializedName("prescribed_medication")
    private List<PrescribedMedication> prescribedMedication;

    public String patientName;
    public ObjectId patientId;

}
