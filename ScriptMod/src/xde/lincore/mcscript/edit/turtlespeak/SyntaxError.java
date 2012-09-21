package xde.lincore.mcscript.edit.turtlespeak;

public class SyntaxError extends Exception {

	public SyntaxError() {
		super();
	}

	public SyntaxError(String message, Throwable cause) {
		super(message, cause);
	}

	public SyntaxError(String message) {
		super(message);
	}

	public SyntaxError(Throwable cause) {
		super(cause);
	}
	
	public SyntaxError(String format, Object... args) {
		super(format.format(format, args));
	}

}
