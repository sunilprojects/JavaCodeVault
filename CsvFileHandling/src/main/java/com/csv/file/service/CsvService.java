package com.csv.file.service;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class CsvService {

    @Autowired
    private MemberRepository repository;
    
    @PersistenceContext
    private EntityManager manager;
    
    @Transactional
    public UploadResponse processCSV(MultipartFile file) {
        int validCount = 0;
        int invalidCount = 0;
        long startTime = System.currentTimeMillis();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {

            Set<String> uniqueChecker = new HashSet<>();//to maintain uniqueness
            String[] columns = reader.readNext(); // Header
            //Checking header is null or not having expected columns
            if (columns == null || columns.length < 14) {
                throw new CsvProcessingException("CSV header is invalid or missing columns");
            }

            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length < 14) {
                    invalidCount++;
                    continue;
                }

                for (int i = 0; i < row.length; i++) {
                    row[i] = row[i] != null ? row[i].trim() : "";
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

//                repository.save(member);
                System.out.println("next line to persist");
                manager.persist(member);
                validCount++;
            }

        } catch (Exception e) {
        	  throw new CsvProcessingException("Internal error occurred: "+e.getMessage() );        }

        long endTime = System.currentTimeMillis();
        return new UploadResponse(validCount,invalidCount,(endTime - startTime),"csv processed successfully");
    }
}

