package xde.lincore.mcscript.spi;

/**
 * This exception is thrown when a script explicitly ends by calling "exit".
 */
public class ExplicitScriptExit extends RuntimeException {
	private final Object exitValue;
		
	public ExplicitScriptExit() {
		exitValue = null;
	}
	
	public ExplicitScriptExit(final Object exitValue) {
		this.exitValue = exitValue;
	}
	
	public Object getExitValue() {
		return exitValue;
	}
}
