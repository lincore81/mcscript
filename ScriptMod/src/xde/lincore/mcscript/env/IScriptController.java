package xde.lincore.mcscript.env;

public interface IScriptController extends Iterable<Script> {

	public void registerScript(Script script);

	public boolean removeScript();

	public boolean hasScript();

	public Script getScript();
	
	public boolean isRunning(Script script);
	
	public void stop(long threadId);

	public long getThreadId();
}