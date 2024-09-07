package com.springboot.business.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessPostDto {

    @Column(nullable = false, length = 20)
    private String BusinessTitle;

}
