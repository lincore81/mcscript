package xde.lincore.mcscript.spi;

public class ScriptInterruptedException extends RuntimeException {

	public ScriptInterruptedException() {
		super();
	}

	public ScriptInterruptedException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ScriptInterruptedException(final String message) {
		super(message);
	}

	public ScriptInterruptedException(final Throwable cause) {
		super(cause);
	}

}
