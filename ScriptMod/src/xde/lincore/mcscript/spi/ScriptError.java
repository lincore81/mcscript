package xde.lincore.mcscript.spi;


public class ScriptError extends RuntimeException {
	/**
	 *
	 */
	private static final long	serialVersionUID	= 5613801294101994663L;
	
	private final String errorDescription;

	public ScriptError() {
		super();
		errorDescription = "No description given.";
	}

	public ScriptError(final String message, final Throwable cause) {
		super(message, cause);
		errorDescription = message;
	}

	public ScriptError(final String message) {
		super(message);
		errorDescription = message;
	}

	public ScriptError(final Throwable cause) {
		super(cause);
		errorDescription = cause.getMessage();
	}
	
	public String getErrorDescription() {
		return errorDescription;
	}
}
