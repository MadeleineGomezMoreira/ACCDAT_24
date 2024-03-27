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

    public Credential(String fileLine) {
        String[] data = fileLine.split(Constants.SEMICOLON);
        this.id = Integer.parseInt(data[0]);
        this.username = data[1];
        this.password = data[2];
    }

    public String toStringTextFile() {
        return this.id + Constants.SEMICOLON +
                this.username + Constants.SEMICOLON +
                this.password;
    }
}
