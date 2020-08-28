package xyz.rpolnx.spring_bank.account.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import xyz.rpolnx.spring_bank.common.exceptions.BadRequestException;
import xyz.rpolnx.spring_bank.common.exceptions.NotFoundException;

public class CustomErrorDecoder implements ErrorDecoder {
    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                return new BadRequestException(response.reason());
            case 404:
                return new NotFoundException(response.reason());
            default:
                return new Exception("Generic error");
        }
    }
}
