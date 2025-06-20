package com.csv.file.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
	   private MemberErrorResponse buildError(HttpStatus status, String message) {
	        return new MemberErrorResponse(
	                status.value(),
	                message,
	                System.currentTimeMillis()
	        );
	    }
	   @ExceptionHandler(CsvProcessingException.class)
	   public ResponseEntity<MemberErrorResponse> handleCsvError(CsvProcessingException e){
		   return new ResponseEntity<>(buildError(HttpStatus.BAD_REQUEST,e.getMessage()),HttpStatus.BAD_REQUEST);
	   } 

	 

}
