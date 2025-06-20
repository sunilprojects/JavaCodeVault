package com.csv.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {
    private int validCount;
    private int invalidCount;
    private long timeTakenMillis;
    private String message;

}
