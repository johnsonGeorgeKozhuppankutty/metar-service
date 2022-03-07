package com.twoeSystems.aviation.exceptions;

/**
 * IcaoCodeAlreadyExistsException.
 * @author The Johnson George.
 */
public class IcaoCodeAlreadyExistsException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
    public IcaoCodeAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }
    public IcaoCodeAlreadyExistsException() {
    }
}