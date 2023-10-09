package com.vaccinationCenter.vaccinationCenter.model;

import com.vaccinationCenter.vaccinationCenter.entity.VaccinationDB;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequiredResponse {

    private VaccinationDB center;
    private List<Citizen> citizens;


}