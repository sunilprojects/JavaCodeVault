package com.csv.file.dto;

import java.time.LocalDate;

public interface MemberDetails {
	  Long getRecordNumber();       // member_record
	    String getFirstName();        // first_name
	    String getLastName();         // last_name
	    LocalDate getDob();           // date_of_birth
	    String getGender();
	    String getCity();
	    Double getMonthlySalary();    // monthly_salary

}
