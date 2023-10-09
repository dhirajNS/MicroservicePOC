package com.citizenService.CitizenService.controller;

import com.citizenService.CitizenService.entity.Citizen;
import com.citizenService.CitizenService.repository.CitizenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citizen")
public class CitizenController {

    @Autowired
    private CitizenRepo repo;

    @RequestMapping(path ="/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }

    @RequestMapping(path ="/id/{id}")
    public ResponseEntity<java.util.List<Citizen>> getById(@PathVariable Integer id) {

        List<Citizen> listCitizen = repo.findByVaccinationCenterId(id);
        return new ResponseEntity<>(listCitizen, HttpStatus.OK);
    }

    @PostMapping(path ="/add")
    public ResponseEntity<Citizen> addCitizen(@RequestBody Citizen newCitizen) {

        Citizen citizen = repo.save(newCitizen);
        return new ResponseEntity<>(citizen, HttpStatus.OK);
    }



}