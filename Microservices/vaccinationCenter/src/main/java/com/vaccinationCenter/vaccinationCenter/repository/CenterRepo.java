package com.vaccinationCenter.vaccinationCenter.repository;


import com.vaccinationCenter.vaccinationCenter.entity.VaccinationDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CenterRepo extends JpaRepository<VaccinationDB, Integer> {

}