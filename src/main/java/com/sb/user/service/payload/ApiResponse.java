package com.sb.user.service.payload;

import org.springframework.http.HttpStatus;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {

	private String message;

	private boolean success;

	private HttpStatus status;
}
