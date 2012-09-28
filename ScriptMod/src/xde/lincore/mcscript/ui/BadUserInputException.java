package xde.lincore.mcscript.ui;

public class BadUserInputException extends RuntimeException {

	/**
	 *
	 */
	private static final long	serialVersionUID	= -8128423382287578081L;

	public BadUserInputException() {
		super();
	}

	public BadUserInputException(final String formatString, final Object... args) {
		super(String.format(formatString, args));
	}

	public BadUserInputException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public BadUserInputException(final String msg) {
		super(msg);
	}

	public BadUserInputException(final Throwable cause) {
		super(cause);
	}

}
