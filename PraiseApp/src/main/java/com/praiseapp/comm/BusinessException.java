package com.praiseapp.comm;

public class BusinessException extends RuntimeException {
	
	private static final long serialVersionUID = -1L;
	
	public BusinessException() {
		super();
	}
	
	public BusinessException(String s) {
        super(s);
    }
	
	public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
	
    public BusinessException(Throwable cause) {
        super(cause);
    }
}
