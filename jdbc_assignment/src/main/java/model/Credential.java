package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credential {

    private int id;
    private String username;
    private String password;
    private int patientId;

    public Credential(String username) {
        this.username = username;
    }

    public Credential(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
