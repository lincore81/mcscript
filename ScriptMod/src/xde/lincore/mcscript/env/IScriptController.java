package xde.lincore.mcscript.env;

public interface IScriptController {

	public void registerScript(Script script);

	public boolean removeScript();

	public boolean hasScript();

	public Script getScript();

	public long getThreadId();
}