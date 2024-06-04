package model.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.bson.types.ObjectId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescribedMedication {

    @SerializedName("name")
    private String name;
    @SerializedName("dose")
    private String dose;

    private ObjectId patientId;

    @Override
    public String toString() {
            return "\n" + "--MEDICATION--" +
                    "\n" + "Name: " + name +
                    "\n" + "Dose: " + dose;
    }

}
