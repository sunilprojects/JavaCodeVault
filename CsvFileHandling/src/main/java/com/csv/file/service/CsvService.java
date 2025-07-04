package com.csv.file.service;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.csv.file.dto.UploadResponse;
import com.csv.file.exceptionhandling.CsvProcessingException;
import com.csv.file.model.Member;
import com.csv.file.model.MemberId;
import com.csv.file.repository.MemberRepository;
import com.opencsv.CSVReader;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CsvService {
	@Autowired
	private MemberRepository memberRepository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public UploadResponse processCSV(MultipartFile file) {
		System.out.println("service");
		int validCount = 0;
		int invalidCount = 0;
		int batchSize = 100;
		List<Member> batchList = new ArrayList<>();
		long startTime = System.currentTimeMillis();
		try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {

			Set<String> uniqueChecker = new HashSet<>();//to maintain uniqueness
			String[] columns = reader.readNext(); // Header
			//Checking header is null or not having expected columns
			if (columns == null || columns.length <14) {
				throw new CsvProcessingException("CSV header is invalid or missing columns");
			}

			String[] row;
			while ((row = reader.readNext()) != null) {
				if (row.length <14) {
					invalidCount++;
					continue;
				}
				//checking fields with null
				for (int i = 0; i < row.length; i++) {
					row[i] = row[i] != null ? row[i].trim() : "";//ternary operator
				}

				// fields are created for necessary validations validations
				String firstName = row[1];
				String lastName = row[2];
				String dobStr = row[3];
				String gender = row[4];
				String education = row[5];
				String houseNumber = row[6];
				String address1 = row[7];
				String address2 = row[8];
				String city = row[9];
				String pincode = row[10];
				String mobile = row[11];
				String company = row[12];
				String salaryStr = row[13];
				String memberRecord=row[0];

				// Basic empty check for important fields
				if (firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty() ||
						dobStr.isEmpty() || mobile.isEmpty()) {
					invalidCount++;
					continue;
				}

				// Validate phone number
				mobile = mobile.replaceAll("[^0-9]", "");
				if (!mobile.matches("^[789]\\d{9}$")) {
					invalidCount++;
					continue;
				}

				// Parse and validate DOB
				LocalDate dob = null;
				for (DateTimeFormatter formatter : List.of(
						DateTimeFormatter.ofPattern("d/M/yyyy"),
						DateTimeFormatter.ofPattern("dd-MM-yyyy"),
						DateTimeFormatter.ofPattern("dd/MM/yyyy")
						)) {
					try {
						dob = LocalDate.parse(dobStr, formatter);
						break;
					} catch (DateTimeParseException ignored) {}
				}
				//if dateOfBitth if future date than add it into invalid
				if (dob == null || dob.isAfter(LocalDate.now()) ||
						Period.between(dob, LocalDate.now()).getYears() > 100) {
					invalidCount++;
					continue;
				}

				//  uniqueness check
				String uniqueKey = firstName + lastName + gender + dobStr;
				if (uniqueChecker.contains(uniqueKey)) {
					invalidCount++;
					continue;
				}
				uniqueChecker.add(uniqueKey);

				// Parse salary
				Double salary = null;
				try {
					salary = Double.parseDouble(salaryStr);
				} catch (NumberFormatException e) {
					invalidCount++;
					continue;
				}
				Integer memberRecord1;
				try {
					memberRecord1 = Integer.parseUnsignedInt(memberRecord);
				} catch (NumberFormatException e) {
					invalidCount++;
					continue; // skip this record if it's not a valid number
				}

				// Create and save entity
				MemberId memberId = new MemberId(firstName, lastName, gender, dob);
				Member member = new Member();
				member.setId(memberId);
				member.setEducation(education);
				member.setHouseNumber(houseNumber);
				member.setAddress1(address1);
				member.setAddress2(address2);
				member.setCity(city);
				member.setPincode(pincode);
				member.setMobile(mobile);
				member.setCompany(company);
				member.setMonthlySalary(salary);
				member.setMemberRecord(memberRecord1);

				    batchList.add(member);
				    validCount++;

				    if (batchList.size() == batchSize) {
				        saveBatch(batchList);  // 🔁 commit batch
				        batchList.clear();
				    }

			}

			if (!batchList.isEmpty()) {
			    saveBatch(batchList);  // Final partial batch
			}
		
		} catch (Exception e) {
			throw new CsvProcessingException("Internal error occurred: "+e.getMessage() );        }

		long endTime = System.currentTimeMillis();
		return new UploadResponse(validCount,invalidCount,(endTime - startTime),"csv processed successfully");
	}

	@Transactional
	public void saveBatch(List<Member> batch) {
		String sql = "INSERT INTO member (first_name, last_name, gender, date_of_birth, " +
		        "education, house_number, address1, address2, city, pincode, mobile, " +
		        "company, monthly_salary, member_record) " +
		        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
		        "ON CONFLICT (date_of_birth, first_name, gender, last_name) DO NOTHING";//Postgres to ignore rows that would violate a unique constraint — no error will be thrown.
    //batchUpdate() is used to execute multiple SQL update/insert/delete operations in one batch
	    jdbcTemplate.batchUpdate(sql, batch, batch.size(), (ps, member) -> {
	        MemberId id = member.getId();
	        ps.setString(1, id.getFirstName());
	        ps.setString(2, id.getLastName());
	        ps.setString(3, id.getGender());
	        ps.setObject(4, id.getDateOfBirth());
	        ps.setString(5, member.getEducation());
	        ps.setString(6, member.getHouseNumber());
	        ps.setString(7, member.getAddress1());
	        ps.setString(8, member.getAddress2());
	        ps.setString(9, member.getCity());
	        ps.setString(10, member.getPincode());
	        ps.setString(11, member.getMobile());
	        ps.setString(12, member.getCompany());
	        ps.setDouble(13, member.getMonthlySalary());
	        ps.setInt(14, member.getMemberRecord());
	    });

	    log.info("Committed batch of size: " + batch.size());
	    System.out.flush();
	}

	
	//getting members based on dob on requirement
	public List<Member> getMembersByDateOfBirth(LocalDate startDate, LocalDate endDate) {
		List<Member> members = memberRepository.findDateOfBirth(startDate, endDate);
		return members;
	}
}

