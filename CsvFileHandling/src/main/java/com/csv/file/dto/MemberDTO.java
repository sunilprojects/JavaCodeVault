package com.csv.file.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class MemberDTO {
	  private long recordNumber;
	    private String firstName;
	    private String lastName;
	    private LocalDate dob;
	    private String gender;
	    private String city;
	  
	

}
