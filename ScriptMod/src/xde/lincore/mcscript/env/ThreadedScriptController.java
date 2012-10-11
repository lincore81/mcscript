package xde.lincore.mcscript.env;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ThreadedScriptController implements IScriptController {
	private final Map<Long, Script> scripts;
	private final ScriptEnvironment env;
	private Script lastScript;

	protected ThreadedScriptController(final ScriptEnvironment env) {
		this.env = env;
		scripts = new HashMap<Long, Script>();
	}


	@Override
	public void registerScript(final Script context) {
		if (context == null) {
			throw new IllegalArgumentException("context must not be null!");
		}
		final long threadId = getThreadId();
		G.LOG.finer(String.format("Registering script thread: %d, %s", threadId, context));
		scripts.put(threadId, context);
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.env.ScriptController#removeContext()
	 */
	@Override
	public boolean removeScript() {
		final long threadId = getThreadId();
		lastScript = scripts.get(threadId);
		return scripts.remove(threadId) != null;
	}

	public Script getLastScript() {
		return lastScript;
	}

	@Override
	public boolean hasScript() {
		final long threadId = getThreadId();
		return scripts.containsKey(threadId);
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.env.ScriptController#getContext()
	 */
	@Override
	public Script getScript() {
		final long threadId = getThreadId();
		return scripts.get(threadId);
	}

	@Override
	public long getThreadId() {
		return Thread.currentThread().getId();
	}


	@Override
	public Iterator<Script> iterator() {
		return scripts.values().iterator();
	}


	@Override
	public boolean isRunning(final Script script) {
		return scripts.containsValue(script);
	}


	@Override
	public void stop(final long threadId) {
		if (scripts.containsKey(threadId) && env.isMultithreaded()) {
			final ThreadGroup grp = Thread.currentThread().getThreadGroup();
			final int threadCount = grp.activeCount();
			final Thread[] threads = new Thread[threadCount];
			grp.enumerate(threads);
			for (int i = 0; i < threadCount; i++) {
				if (threads[i].getId() == threadId) {
					threads[i].interrupt();
				}
			}
		}
		else throw new IllegalArgumentException();
	}
}
