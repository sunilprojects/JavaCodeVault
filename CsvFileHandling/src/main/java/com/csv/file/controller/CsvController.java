package com.csv.file.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csv.file.dto.UploadResponse;
import com.csv.file.exceptionhandling.CsvProcessingException;
import com.csv.file.service.CsvService;
@RestController
@RequestMapping("/api")
public class CsvController {

    @Autowired
    private CsvService csvService;

    @PostMapping("/csvfileupload")
    public ResponseEntity<UploadResponse> uploadCSV(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
        	 throw new CsvProcessingException(" Error with file: Please upload csv file " );        
        }
        UploadResponse result = csvService.processCSV(file);//calling service to process csv
        return ResponseEntity.ok(result);
    }
}
