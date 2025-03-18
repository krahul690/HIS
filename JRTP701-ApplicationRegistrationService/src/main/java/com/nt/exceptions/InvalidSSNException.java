package com.nt.exceptions;

public class InvalidSSNException extends Exception {
	
	public InvalidSSNException() {
	   super();
	}
	
	public   InvalidSSNException(String  msg) {
		super(msg);
	}

}
