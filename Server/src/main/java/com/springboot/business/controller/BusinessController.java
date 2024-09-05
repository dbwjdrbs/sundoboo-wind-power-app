package com.springboot.business.controller;

import com.springboot.business.dto.BusinessResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/businesses")
@Validated
public class BusinessController {
    @GetMapping()
    public ResponseEntity helloWorld(){
        BusinessResponseDto response = new BusinessResponseDto(1,"김영진");
        return new ResponseEntity( response ,HttpStatus.OK);
    }
}
