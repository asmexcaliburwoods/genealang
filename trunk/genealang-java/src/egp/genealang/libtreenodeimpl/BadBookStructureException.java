package egp.genealang.libtreenodeimpl;

public class BadBookStructureException extends Exception{
	private static final long serialVersionUID = -6901473252105339432L;

	public BadBookStructureException() {
		super();
	}

	public BadBookStructureException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadBookStructureException(String message) {
		super(message);
	}

	public BadBookStructureException(Throwable cause) {
		super(cause);
	}
}