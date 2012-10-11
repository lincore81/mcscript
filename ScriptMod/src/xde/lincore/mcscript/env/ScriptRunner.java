package xde.lincore.mcscript.env;

import javax.script.ScriptException;

import xde.lincore.mcscript.spi.ScriptError;
import xde.lincore.mcscript.spi.ScriptInterruptedException;



public class ScriptRunner implements Runnable {

	private final Script script;
	private final ScriptEnvironment env;

	public Object result;
	private ScriptArguments args;

	public ScriptRunner(final Script script, final ScriptEnvironment env) {
		this.script = script;
		this.env = env;
	}

	@Override
	public void run() {
		preExecution();
		try {
			try {
				if (script.isScriptFile()) {
					script.compile();
				}
				script.eval();
				postExecution();
			} catch (final ScriptException e) {
				if (e.getCause() != null) {
					throw e.getCause();
				} else {
					throw e;
				}
			}
		} catch (final ScriptError e) {
			env.chat.err(e.getErrorDescription());
		} catch (ScriptInterruptedException e) {
			env.chat.err(String.format("Script interrupted: %s, §e(%d)",
					script.toString(), script.getThreadId()));
		} catch (final Throwable e) {
			env.setLastScriptException(e);
			printException(e);
		} finally {
			env.scripts.removeScript();
		}
	}
	
	private void preExecution() {
		script.setBindings(script.isScriptFile() ? env.getFileBindings() : env.getCuiBindings());
		env.scripts.registerScript(script);
		script.setThreadId(env.scripts.getThreadId());
	}
	
	private void postExecution() {
		if (script.isScriptFile()) {
			env.setFileBindings(script.getBindings());
		} else {
			env.setCuiBindings(script.getBindings());
		}
	}

	private void printException(final Throwable e) {

		String msg = e.getMessage();
		if (msg == null) {
			env.chat.err(e.toString());
		}
		else {
			String exceptionName = null;
			if (msg.contains(":")) {
				final String[] msgparts = e.getMessage().split(":", 2);
				msg = msgparts[0];
				exceptionName = msgparts[1];
			}
			if (exceptionName != null) {
				env.chat.err(msg + " §f(" + exceptionName + ")");
			} else {
				env.chat.err(msg);
			}
		}
		env.setLastScriptException(e);
		e.printStackTrace();
		if (e.getCause() != null) {
			e.getCause().printStackTrace();
		}
	}
}
