package xde.lincore.mcscript.ui;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import net.minecraft.server.MinecraftServer;
import xde.lincore.mcscript.env.ScriptEnvironment;

public final class McChatLogHandler extends Handler {

	private static long pointZero = System.currentTimeMillis();
	private final ScriptEnvironment env;


	public McChatLogHandler(final ScriptEnvironment env) {
		super();
		this.env = env;
		setFormatter(new SimpleFormatter());
	}


	@Override
	public void close() throws SecurityException {}

	@Override
	public void flush() {
		System.out.flush();
	}

	@Override
	public void publish(final LogRecord record) {
		if (!isLoggable(record)) {
			return;
		}

		final String msg = getFormatter().format(record);
		if (isServerRunning()) {
			env.chat.echo(msg);
			System.out.println(msg);
		}
		else {
			System.out.println(msg);
		}
	}

	private boolean isServerRunning() {
		return MinecraftServer.getServer() != null && !MinecraftServer.getServer().isDemo();
	}

}
