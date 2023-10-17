package com.vaccinationCenter.vaccinationCenter.controller;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.vaccinationCenter.vaccinationCenter.entity.VaccinationDB;
import com.vaccinationCenter.vaccinationCenter.model.Citizen;
import com.vaccinationCenter.vaccinationCenter.model.CitizenWrapper;
import com.vaccinationCenter.vaccinationCenter.model.RequiredResponse;
import com.vaccinationCenter.vaccinationCenter.repository.CenterRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

//    @GetMapping(path = "/id/{id}")
//    //@CircuitBreaker(name=VACCINE_SERVICE,fallbackMethod = "handleFallBack")
//    //@Retry(name = VACCINE_SERVICE,fallbackMethod = "handleFallBack")
//    @Retry(name = VACCINE_SERVICE)     //when using exception handling and there is no fallback in retry
//    public ResponseEntity<RequiredResponse> getAllDadaBasedonCenterId(@PathVariable Integer id){
//
//            RequiredResponse requiredResponse = new RequiredResponse();
//            VaccinationDB center = centerRepo.findById(id).get();
//            requiredResponse.setCenter(center);
//            logger.info("Hi before rest call******************");
//            // then get all citizen registerd to vaccination center
//            java.util.List<Citizen> listOfCitizens = restTemplate.getForObject("http://CITIZEN-SERVICE/citizen/id/" + id, List.class);
//            requiredResponse.setCitizens(listOfCitizens);
//            logger.info("Hi after rest call>>{}",listOfCitizens);
//            return new ResponseEntity<>(requiredResponse, HttpStatus.OK);
//    }

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

    @GetMapping(path = "/id/{id}")
    //@CircuitBreaker(name=VACCINE_SERVICE,fallbackMethod = "handleFallBack")
    //@Retry(name = VACCINE_SERVICE,fallbackMethod = "handleFallBack")
    @Retry(name = VACCINE_SERVICE)     //when using exception handling and there is no fallback in retry
    public ResponseEntity<RequiredResponse> getAllDadaBasedonCenterId(@PathVariable Integer id){

        RequiredResponse requiredResponse = new RequiredResponse();
        VaccinationDB center = centerRepo.findById(id).get();
        requiredResponse.setCenter(center);
        logger.info("Hi before rest call******************");
        // then get all citizen registerd to vaccination center


        String targetUrl = "http://localhost:8080/citizen/id/" + id;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        Object password1 = authentication.getCredentials();
        String username = "Java Techie";
        String password="Password";
        // Create a HttpHeaders object with Basic Authentication credentials
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        // Create an HttpEntity with the headers
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        // Make the REST request with Basic Authentication
        java.util.List<Citizen> listOfCitizens  = new RestTemplate().exchange(
                targetUrl,
                HttpMethod.GET,
                entity, // Pass the HttpEntity with Basic Authentication headers
                List.class // Specify the response type
        ).getBody();
        requiredResponse.setCitizens(listOfCitizens);
        logger.info("Hi after rest call>>{}",listOfCitizens);
        return new ResponseEntity<>(requiredResponse, HttpStatus.OK);
    }


}
