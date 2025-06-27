package com.csv.file.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.csv.file.dto.MemberDTO;
import com.csv.file.dto.MemberDetails;
import com.csv.file.model.Member;
import com.csv.file.model.MemberId;

import jakarta.transaction.Transactional;


public interface MemberRepository  extends JpaRepository<Member, MemberId>{
	  @Modifying
	    @Transactional
	    @Query(value = """
	        INSERT INTO member (
	            first_name, last_name, gender, date_of_birth,
	            education, house_number, address1, address2,
	            city, pincode, mobile, company, monthly_salary, member_record
	        )
	        VALUES (
	            :firstName, :lastName, :gender, :dob,
	            :education, :houseNumber, :address1, :address2,
	            :city, :pincode, :mobile, :company, :monthlySalary, :memberRecord
	        )
	        ON CONFLICT (date_of_birth, first_name, gender, last_name) DO NOTHING
	    """, nativeQuery = true)
	    void insertMember(
	        @Param("firstName") String firstName,
	        @Param("lastName") String lastName,
	        @Param("gender") String gender,
	        @Param("dob") LocalDate dob,
	        @Param("education") String education,
	        @Param("houseNumber") String houseNumber,
	        @Param("address1") String address1,
	        @Param("address2") String address2,
	        @Param("city") String city,
	        @Param("pincode") String pincode,
	        @Param("mobile") String mobile,
	        @Param("company") String company,
	        @Param("monthlySalary") Double monthlySalary,
	        @Param("memberRecord") Integer memberRecord
	    );

	
	
	
	//JPQL Query
	@Query("""
		    SELECT new com.csv.file.dto.MemberDTO(
		        m.memberRecord,
		        m.id.firstName,
		        m.id.lastName,
		        m.id.dateOfBirth,
		        m.id.gender,
		        m.city
		    )
		    FROM Member m
		    WHERE m.id.firstName LIKE CONCAT(:fName, '%')
		      AND m.id.lastName LIKE  CONCAT(:lName, '%')
		""")
		List<MemberDTO> findMatchRecords(@Param("fName") String firstName,
				                          @Param("lName") String lastName );
	
	//Jpql Query
	@Query("""
			SELECT m 
			FROM Member m
			 WHERE m.id.dateOfBirth BETWEEN :startDate AND :endDate
			""")
			List<Member> findDateOfBirth(@Param("startDate") LocalDate startDate,
	                                        @Param("endDate") LocalDate endDate);
	

 @Query(value="""
 		SELECT member_record AS recordNumber,
 	    first_name AS firstName, 
 	    last_name AS lastName,
 	    date_of_birth AS dob,
 	    gender, 
 	    city, 
 	    monthly_salary AS monthlySalary 
 	    FROM member
        WHERE monthly_salary >=:sal
 		""", nativeQuery = true)
    List<MemberDetails> findHighestSalaryMembers(@Param(value="sal") double salary);
 
 Page<Member> findAll(Pageable pageable);
	    
}
