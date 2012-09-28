package xde.lincore.mcscript.env;

import xde.lincore.mcscript.minecraft.ScriptError;

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
		script.setBindings(script.isScriptFile() ? env.getFileBindings() : env.getCuiBindings());
		env.scripts.registerScript(script);
		try {
			if (script.isScriptFile()) {
				script.compile();
			}
			script.eval();
			if (script.isScriptFile()) {
				env.setFileBindings(script.getBindings());
			} else {
				env.setCuiBindings(script.getBindings());
			}
		} catch (final ScriptError e) {
			env.chat.err("Error: " + e.getMessage());
		} catch (final Exception e) {
			env.setLastScriptException(e);
			printException(e);
		} finally {
			env.scripts.removeScript();
		}
	}

	private void printException(final Exception e) {

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
