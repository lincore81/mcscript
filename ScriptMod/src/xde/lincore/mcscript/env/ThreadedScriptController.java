package xde.lincore.mcscript.env;

import java.util.HashMap;
import java.util.Map;

public class ThreadedScriptController implements IScriptController {
	private Map<Long, Script> scripts;
	private ScriptEnvironment env;
	private Script lastScript;
	
	protected ThreadedScriptController(ScriptEnvironment env) {
		this.env = env;
		scripts = new HashMap<Long, Script>();
	}
	

	@Override
	public void registerScript(Script context) {
		if (context == null) {
			throw new IllegalArgumentException("context must not be null!");
		}
		long threadId = getThreadId();
		scripts.put(threadId, context);
	}
	
	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.env.ScriptController#removeContext()
	 */
	@Override
	public boolean removeScript() {
		long threadId = getThreadId();
		lastScript = scripts.get(threadId);
		return scripts.remove(threadId) != null;		
	}
	
	public Script getLastScript() {
		return lastScript;
	}
	
	@Override
	public boolean hasScript() {
		long threadId = getThreadId();
		return scripts.containsKey(threadId);
	}
	
	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.env.ScriptController#getContext()
	 */
	@Override
	public Script getScript() {
		long threadId = getThreadId();
		return scripts.get(threadId);
	}
	
	@Override
	public long getThreadId() {
		return Thread.currentThread().getId();
	}
	
}
