package be.peopleware.facebookdemo.security.dwr;

public class FacebookSecurityException extends SecurityException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2670447231365414007L;

	public FacebookSecurityException(String message) {
		super(message);
	}

	public FacebookSecurityException(String message, Throwable cause) {
		super(message, cause);
	}
}
