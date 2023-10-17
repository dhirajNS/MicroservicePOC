package com.vaccinationCenter.vaccinationCenter.exceptionHandler;

import com.vaccinationCenter.vaccinationCenter.controller.VaccinationCenterController;
import com.vaccinationCenter.vaccinationCenter.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class CustomExceptionHandler {
    private static final Logger logger= LoggerFactory.getLogger(VaccinationCenterController.class);

    @ExceptionHandler({NullPointerException.class, HttpServerErrorException.class,IllegalStateException.class, ConnectException.class})
    public ResponseEntity<ErrorResponse> nullAndConnectionHandler(RuntimeException ex) throws ParseException {
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setErrorCode(500);
        errorResponse.setErrorMessage("Something went wrong!!");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date currentDate = new Date();
        String formattedDate = formatter.format(currentDate);
        errorResponse.setErrorDetails(ex.getMessage());
        errorResponse.setDateTime(formattedDate);
        logger.info("------------------inside nullAndConnectionHandler---------------");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> allOtherExceptions(RuntimeException ex) throws ParseException {
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setErrorCode(500);
        errorResponse.setErrorMessage("Something went wrong!!");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date currentDate = new Date();
        String formattedDate = formatter.format(currentDate);
        errorResponse.setErrorDetails(ex.getMessage());
        errorResponse.setDateTime(formattedDate);
        logger.info("------------------inside allOtherExceptions---------------");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
