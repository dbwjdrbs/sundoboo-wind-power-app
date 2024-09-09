package com.springboot.business.entity;


import com.springboot.auditable.Auditable;
import com.springboot.businessscore.entity.BusinessScore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Business extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long businessId;

    @Column(nullable = false, length = 20)
    private String businessTitle;

    @OneToMany(mappedBy = "business", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<BusinessScore> businessScoreList = new ArrayList<>();

}
