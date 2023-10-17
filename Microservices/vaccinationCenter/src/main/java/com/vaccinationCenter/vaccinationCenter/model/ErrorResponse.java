package com.vaccinationCenter.vaccinationCenter.model;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorResponse {
    int errorCode;
    String errorMessage;
    String dateTime;
    String errorDetails;

}
