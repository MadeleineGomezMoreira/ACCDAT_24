package model.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.*;

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

}
