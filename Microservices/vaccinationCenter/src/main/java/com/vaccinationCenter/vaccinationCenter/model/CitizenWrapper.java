package com.vaccinationCenter.vaccinationCenter.model;

import lombok.Data;

import java.util.List;
@Data
public class CitizenWrapper {
    String code;    //http status code
    List<Citizen> data;

}
