package xde.lincore.mcscript.ui;

public class BadUserInputException extends RuntimeException {

	public BadUserInputException() {
		super();
	}
	
	public BadUserInputException(String formatString, Object... args) {
		super(String.format(formatString, args));
	}

	public BadUserInputException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public BadUserInputException(String msg) {
		super(msg);
	}

	public BadUserInputException(Throwable cause) {
		super(cause);
	}

}
