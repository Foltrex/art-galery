package com.scnsoft.art.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ErrorDto {
    private Date timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
