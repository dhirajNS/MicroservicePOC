package com.citizenService.CitizenService.repository;

import com.citizenService.CitizenService.entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitizenRepo extends JpaRepository<Citizen, Integer> {

    public List<Citizen> findByVaccinationCenterId(Integer id);

}
