package com.csv.file.exceptionhandling;

public class CsvProcessingException extends RuntimeException{
	public CsvProcessingException(String message) {
		super(message);
	}

}
