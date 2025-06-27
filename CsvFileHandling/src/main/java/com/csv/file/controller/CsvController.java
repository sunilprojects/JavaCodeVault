package com.csv.file.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csv.file.dto.MemberDTO;
import com.csv.file.dto.MemberDetails;
import com.csv.file.dto.UploadResponse;
import com.csv.file.exceptionhandling.CsvProcessingException;
import com.csv.file.model.Member;
import com.csv.file.repository.MemberRepository;
import com.csv.file.service.CsvSingleTransactionService;
import com.csv.file.service.CsvService;
@RestController
@RequestMapping("/api")
public class CsvController {

    @Autowired
    private CsvService csvService;
   
    @Autowired
    private  CsvSingleTransactionService singleTransactionService; 
    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/csvfileupload")
    public ResponseEntity<UploadResponse> uploadCSV(@RequestParam("file") MultipartFile file) {
    	System.out.println("controller");
        if (file.isEmpty()) {
        	 throw new CsvProcessingException(" Error with file: Please upload csv file " );        
        }
        UploadResponse result = csvService.processCSV(file);//calling service to process csv
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/csvfileupload1/singletransaction")
    public ResponseEntity<UploadResponse> uploadCsvSingleTran(@RequestParam("file") MultipartFile file) {
    	System.out.println("controller");
        if (file.isEmpty()) {
        	 throw new CsvProcessingException(" Error with file: Please upload csv file " );        
        }
        UploadResponse result = singleTransactionService.processCSV(file);//calling service to process csv
        return ResponseEntity.ok(result);
    }
    
    
    @GetMapping("/matchrecord")//getting matched records based on requirements 
    public ResponseEntity<List<MemberDTO>> findMatchRecords(@RequestParam String firstName,
    		                                                @RequestParam String lastName) {
    	     List<MemberDTO> records= memberRepository.findMatchRecords(firstName,lastName);
    	     return ResponseEntity.ok(records);
    }
    
    @GetMapping("/dateofbirth")//finding members based on range of date of birth
    public ResponseEntity<List<Member>> getMembersByDateOfBirth(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
            //@DateTimeFormat-automatic parsing
        List<Member> members = csvService.getMembersByDateOfBirth(startDate, endDate);
        return ResponseEntity.ok(members);
    }
    
    @GetMapping("/highestsalary")
    public ResponseEntity<List<MemberDetails>> getHighSalaryMembers(@RequestParam("salary") Double salary) {
    	List<MemberDetails> highSalary=memberRepository.findHighestSalaryMembers(salary);
        return ResponseEntity.ok(highSalary);
    }
    
    @GetMapping("/findallrecords")
    public List<Member> findAllRecords() {
    	List<Member> result=memberRepository.findAll();
    	return result;
    }
    
  
    //backend pagination and sorting records
    @GetMapping("/paginated")
    public Page<Member> getMembersWithDefault(
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam(defaultValue = "memberRecord") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir
    ) {
    	Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size,sort);//creates pagination object
        return memberRepository.findAll(pageable);
    }


    
    	
    
}
