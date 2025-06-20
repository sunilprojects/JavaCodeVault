package com.csv.file.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Member {

    @EmbeddedId
    private MemberId id;

    private String education;
    private String houseNumber;
    private String address1;
    private String address2;
    private String city;
    private String pincode;
    private String mobile;
    private String company;
    private Double monthlySalary;

   
}
