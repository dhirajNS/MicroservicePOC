package com.citizenService.CitizenService.controller;

import com.citizenService.CitizenService.entity.Citizen;
import com.citizenService.CitizenService.repository.CitizenRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/citizen")
public class CitizenController {

    @Autowired
    private CitizenRepo repo;
    private static final Logger logger= LoggerFactory.getLogger(CitizenController.class);

    @RequestMapping(path ="/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }

    @RequestMapping(path ="/id/{id}")
    public ResponseEntity<java.util.List<Citizen>> getById(@PathVariable Integer id) {
        try {
            logger.info("Before success in Citizen");
            List<Citizen> listCitizen = repo.findByVaccinationCenterId(id);
//            Citizen citizen=null;
//            citizen.setId(1);
            logger.info("After success in Citizen");
            return new ResponseEntity<>(listCitizen, HttpStatus.OK);
        }catch (Exception e){
            logger.error("Something went wrong>>>"+e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path ="/add")
    public ResponseEntity<Citizen> addCitizen(@RequestBody Citizen newCitizen) {

        Citizen citizen = repo.save(newCitizen);
        return new ResponseEntity<>(citizen, HttpStatus.OK);
    }



}