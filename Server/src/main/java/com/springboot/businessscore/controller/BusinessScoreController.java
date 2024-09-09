package com.springboot.businessscore.controller;


import com.springboot.businessscore.dto.BusinessScoreDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/Scores")
@Validated
public class BusinessScoreController {

    @PostMapping
    public ResponseEntity postScore(@Valid @RequestBody BusinessScoreDto.Post requestBody){
        return null;
    };
}
