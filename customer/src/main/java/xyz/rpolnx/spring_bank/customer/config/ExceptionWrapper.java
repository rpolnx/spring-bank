package xyz.rpolnx.spring_bank.customer.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Data
public class ExceptionWrapper {
    public String cause;
    public int httpCode;
    private String message;

    public ExceptionWrapper(HttpStatus status, String message) {
        this.cause = status.getReasonPhrase();
        this.httpCode = status.value();
        this.message = message;
    }
}
