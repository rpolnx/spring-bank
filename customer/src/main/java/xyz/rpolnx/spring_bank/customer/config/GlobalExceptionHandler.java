package xyz.rpolnx.spring_bank.customer.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import xyz.rpolnx.spring_bank.customer.exceptions.BadRequestException;
import xyz.rpolnx.spring_bank.customer.exceptions.NotFoundException;

import java.sql.SQLException;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@ResponseBody
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ExceptionWrapper handleException(BadRequestException ex) {
        return new ExceptionWrapper(NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ExceptionWrapper handleException(NotFoundException ex) {
        return new ExceptionWrapper(NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionWrapper handleException(SQLException ex) {
        log.error("Sql exception: ", ex);

        return new ExceptionWrapper(UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ExceptionWrapper handleException(Exception ex) {
        log.error("Unexpected error: ", ex);

        return new ExceptionWrapper(INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
