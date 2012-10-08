package xde.lincore.mcscript.spi;

/**
 * This exception is thrown when a script explicitly ends by calling "exit".
 */
public class ScriptEnd extends RuntimeException {
	private final Object exitValue;
		
	public ScriptEnd() {
		exitValue = null;
	}
	
	public ScriptEnd(final Object exitValue) {
		this.exitValue = exitValue;
	}
	
	public Object getExitValue() {
		return exitValue;
	}
}
