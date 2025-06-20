package com.csv.file.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberErrorResponse {
    private int status;
    private String message;
    private long timeStamp;

	
}
