package model.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.bson.types.ObjectId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor {

    @SerializedName("_id")
    private ObjectId doctorId;
    @SerializedName("name")
    private String name;
    @SerializedName("specialty")
    private String specialty;
    @SerializedName("phone")
    private String phone;

}
