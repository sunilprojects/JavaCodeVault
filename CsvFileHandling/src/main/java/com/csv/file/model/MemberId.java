package com.csv.file.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class MemberId implements Serializable {
	private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;

    public MemberId() {}

    public MemberId(String firstName, String lastName, String gender, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters, setters, equals() and hashCode()
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberId)) return false;
        MemberId memberId = (MemberId) o;
        return Objects.equals(firstName, memberId.firstName) &&
               Objects.equals(lastName, memberId.lastName) &&
               Objects.equals(gender, memberId.gender) &&
               Objects.equals(dateOfBirth, memberId.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, gender, dateOfBirth);
    }
	


	

}
