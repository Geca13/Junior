package com.example.junior.domain;

import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse {
	
	private int httpStatusCode;
	
	private HttpStatus httpStatus;
	
	private String reason;
	
	private String message;

}
