/**
 * 
 */
package egp.sphere.model;

public class DuplicateLabelFoundException extends Exception{

	public DuplicateLabelFoundException() {
		super();
	}

	public DuplicateLabelFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateLabelFoundException(String message) {
		super(message);
	}

	public DuplicateLabelFoundException(Throwable cause) {
		super(cause);
	}
}