package com.ankitkumar.library.exception;

public class DuplicateEmailException extends RuntimeException{

	public DuplicateEmailException(String message) {
		super(message);
	}
}
