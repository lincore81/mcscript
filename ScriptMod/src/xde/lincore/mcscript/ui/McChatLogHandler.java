package xde.lincore.mcscript.ui;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import org.bouncycastle.util.Strings;

import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.mcscript.minecraft.MinecraftWrapper;
import xde.lincore.util.StringTools;


import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.StringUtils;

public final class McChatLogHandler extends Handler {

	private static long pointZero = System.currentTimeMillis();
	private ScriptEnvironment env;

	
	public McChatLogHandler(ScriptEnvironment env) {
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
	public void publish(LogRecord record) {		
		if (!isLoggable(record)) return;
		
		String msg = getFormatter().format(record);
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
