package xde.lincore.mcscript.edit.turtlespeak;

public class SyntaxError extends Exception {

	/**
	 *
	 */
	private static final long	serialVersionUID	= -2812622465298486467L;

	public SyntaxError() {
		super();
	}

	public SyntaxError(final String message, final Throwable cause) {
		super(message, cause);
	}

	public SyntaxError(final String message) {
		super(message);
	}

	public SyntaxError(final Throwable cause) {
		super(cause);
	}

	public SyntaxError(final String format, final Object... args) {
		super(String.format(format, args));
	}

}
