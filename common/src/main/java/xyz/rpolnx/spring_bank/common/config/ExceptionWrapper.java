package xyz.rpolnx.spring_bank.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@NoArgsConstructor
@Data
@JsonInclude(NON_NULL)
public class ExceptionWrapper {
    public String cause;
    public int httpCode;
    private String message;
    private List<ObjectError> specificErrors;

    public ExceptionWrapper(HttpStatus status, String message) {
        this.cause = status.getReasonPhrase();
        this.httpCode = status.value();
        this.message = message;
    }

    public ExceptionWrapper(HttpStatus status, List<ObjectError> specificErrors) {
        this.cause = status.getReasonPhrase();
        this.httpCode = status.value();
        this.specificErrors = specificErrors;
    }
}
