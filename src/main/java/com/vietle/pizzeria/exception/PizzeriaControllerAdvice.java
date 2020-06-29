package com.vietle.pizzeria.exception;

import com.vietle.pizzeria.domain.Status;
import com.vietle.pizzeria.domain.response.Response;
import com.vietle.pizzeria.util.PizzeriaUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class PizzeriaControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PizzeriaException.class)
    public ResponseEntity<Response> handlePizzeriaException(PizzeriaException ex, WebRequest request) {
        Status status = Status.builder().statusCd(ex.getStatusCd()).message(ex.getMessage()).timestamp(PizzeriaUtil.getTimestamp()).build();
        int statusCd = ex.getStatusCd();
        Response response = new Response(status);
        return new ResponseEntity<>(response, HttpStatus.resolve(statusCd));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleGenericException(Exception ex, WebRequest request) {
        Status status = Status.builder().statusCd(500).message(ex.getMessage()).timestamp(PizzeriaUtil.getTimestamp()).build();
        Response response = new Response(status);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
