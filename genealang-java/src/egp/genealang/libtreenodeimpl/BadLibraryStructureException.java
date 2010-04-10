/**
 * 
 */
package egp.genealang.libtreenodeimpl;

public class BadLibraryStructureException extends Exception{
	private static final long serialVersionUID = -1026080614301737091L;

	public BadLibraryStructureException() {
		super();
	}

	public BadLibraryStructureException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadLibraryStructureException(String message) {
		super(message);
	}

	public BadLibraryStructureException(Throwable cause) {
		super(cause);
	}
}