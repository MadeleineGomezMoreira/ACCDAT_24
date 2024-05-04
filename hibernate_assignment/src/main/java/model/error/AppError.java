package model.error;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class AppError {

    private String message;
    private LocalDateTime errorDateTime = LocalDateTime.now();

    public AppError(String message) {
        this.message = message;
    }
}
