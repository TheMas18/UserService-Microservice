package com.sb.user.service.exceptions;

public class ResourceNotFoundException  extends RuntimeException{

	public ResourceNotFoundException() {
		super("Resource Not Found On Servcer");
	}
	
	public ResourceNotFoundException(String messsage) {
		super(messsage);
	}
	
}
