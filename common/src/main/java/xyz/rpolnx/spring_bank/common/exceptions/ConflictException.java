package xyz.rpolnx.spring_bank.common.exceptions;

public class ConflictException extends RuntimeException{
    public ConflictException(String message) {
        super(message);
    }
}
