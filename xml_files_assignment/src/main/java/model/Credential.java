package model;

import common.Constants;
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

    public Credential(String username, String password) {
        this.id = 0;
        this.username = username;
        this.password = password;
    }


}
