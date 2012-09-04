package xde.lincore.mcscript;

public class BadUserInput extends RuntimeException {

	public BadUserInput() {
		super();
	}
	
	public BadUserInput(String formatString, Object... args) {
		super(String.format(formatString, args));
	}

	public BadUserInput(String msg, Throwable cause) {
		super(msg, cause);
	}

	public BadUserInput(String msg) {
		super(msg);
	}

	public BadUserInput(Throwable cause) {
		super(cause);
	}

}
