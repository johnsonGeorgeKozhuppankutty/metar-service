package com.twoeSystems.aviation.exceptions;

/**
 * ErrorResponse.
 * @author The Johnson George.
 */
public class ErrorResponse {


	  public ErrorResponse(String message) {
	    super();
	    this.setMessage(message);
	   
	  }
	 
	  public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	//General error message about nature of error
	  private String message;
}
