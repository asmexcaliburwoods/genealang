/**
 * 
 */
package egp.sphere.model;

public class LabelNotFoundException extends Exception{
	public LabelNotFoundException() {
		super();
	}

	public LabelNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public LabelNotFoundException(String message) {
		super(message);
	}

	public LabelNotFoundException(Throwable cause) {
		super(cause);
	}
}