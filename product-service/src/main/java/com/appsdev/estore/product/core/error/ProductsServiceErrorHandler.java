package com.appsdev.estore.product.core.error;

import java.util.Date;
import javax.naming.CommunicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class ProductsServiceErrorHandler {

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException exception, WebRequest webRequest) {
        exception.printStackTrace();
        return getObjectResponseEntity(exception.getMessage(), exception.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception, WebRequest webRequest) {
        return getObjectResponseEntity(exception.getMessage(), exception.getMessage());
    }

    @ExceptionHandler({CommunicationException.class})
    public ResponseEntity<Object> handleCommunicationException(CommunicationException exception,
            WebRequest webRequest) {
        exception.printStackTrace();
        return getObjectResponseEntity(exception.getMessage(), exception.getMessage());
    }

    private static ResponseEntity<Object> getObjectResponseEntity(String exception, String exception1) {
        log.error(exception);
        return new ResponseEntity<>(
                ErrorMessage.builder().timestamp(new Date()).message(exception1).build(), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
