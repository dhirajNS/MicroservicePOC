package com.vaccinationCenter.vaccinationCenter.controller;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.vaccinationCenter.vaccinationCenter.entity.VaccinationDB;
import com.vaccinationCenter.vaccinationCenter.model.Citizen;
//import com.vaccinationCenter.vaccinationCenter.model.CitizenWrapper;
import com.vaccinationCenter.vaccinationCenter.model.RequiredResponse;
import com.vaccinationCenter.vaccinationCenter.repository.CenterRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/vaccinationcenter")
public class VaccinationCenterController {

    @Autowired
    private CenterRepo centerRepo;

    @Autowired
    private RestTemplate restTemplate;
    public static final String VACCINE_SERVICE="vaccineService";
    private int attempt=1;

    private static final Logger logger= LoggerFactory.getLogger(VaccinationCenterController.class);

    @PostMapping(path ="/add")
    public ResponseEntity<VaccinationDB> addVaccineCenter(@RequestBody VaccinationDB vaccinationCenter) {

        VaccinationDB vaccinationCenterAdded = centerRepo.save(vaccinationCenter);
        return new ResponseEntity<>(vaccinationCenterAdded, HttpStatus.OK);
    }

    @GetMapping(path = "/id/{id}")
    //@CircuitBreaker(name=VACCINE_SERVICE,fallbackMethod = "handleFallBack")
    //@Retry(name = VACCINE_SERVICE,fallbackMethod = "handleFallBack")
    @Retry(name = VACCINE_SERVICE)     //when using exception handling and there is no fallback in retry
    @RolesAllowed("user")
    public ResponseEntity<RequiredResponse> getAllDadaBasedonCenterId(@PathVariable Integer id){

            RequiredResponse requiredResponse = new RequiredResponse();
        //try {
            //1st get vaccination center detail
            VaccinationDB center = centerRepo.findById(id).get();
            requiredResponse.setCenter(center);
            logger.info("Hi before rest call******************");
            // then get all citizen registerd to vaccination center
            java.util.List<Citizen> listOfCitizens = restTemplate.getForObject("http://CITIZEN-SERVICE/citizen/id/" + id, List.class);
            requiredResponse.setCitizens(listOfCitizens);
            logger.info("Hi after rest call>>{}",listOfCitizens);

            return new ResponseEntity<>(requiredResponse, HttpStatus.OK);
//        }catch(Exception e){
//            logger.error("Hi with error  ******>>"+id+"<><><><>"+e.getMessage());
//            return new ResponseEntity<RequiredResponse>(requiredResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

//    public ResponseEntity<RequiredResponse> handleFallBack(@PathVariable Integer id){
//        RequiredResponse requiredResponse =  new RequiredResponse();
//        //1st get vaccination center detail
//        VaccinationDB center  = centerRepo.findById(id).get();
//        requiredResponse.setCenter(center);
//        return new ResponseEntity<RequiredResponse>(requiredResponse, HttpStatus.OK);
//    }

    public ResponseEntity<RequiredResponse> handleFallBack(Integer id,Exception ex){
        RequiredResponse requiredResponse =  new RequiredResponse();
        //1st get vaccination center detail
        VaccinationDB center  = centerRepo.findById(id).get();
        requiredResponse.setCenter(center);
        return new ResponseEntity<RequiredResponse>(requiredResponse, HttpStatus.OK);
    }





}
