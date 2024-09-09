package com.springboot.location.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponseDto {
    @Column(nullable = false, length = 20)
    private long businessId;


    // 이거는 후순위로 넣는건가? 애매띠
    @Column(nullable = false, length = 20)
    private long turbineId;

    @Column(nullable = false, length = 20)
    private long locationId;

    private String businessTitle;

    private String modelName;

    @Column(length = 100)
    private String latitude;

    @Column(length = 100)
    private String longitude;

    @Column(length = 20)
    private String city;

    @Column(length = 20)
    private String island;


    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt;

}
