package common.config;

import common.Constants;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Properties;
@Singleton
@Getter
@Log4j2
public class LoginConfiguration {

    private String username;
    private String password;

    public LoginConfiguration() {
        try {
            Properties properties = new Properties();
            properties.load(LoginConfiguration.class.getClassLoader().getResourceAsStream(Constants.USERS_FILE_PATH));
            this.username = properties.getProperty(Constants.USER_CONFIG);
            this.password = properties.getProperty(Constants.PASSWORD_CONFIG);
        } catch (IOException ex) {
            log.error(ex);
        }

    }

}
