package xde.lincore.mcscript.ui;

import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xde.lincore.mcscript.G;
import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.util.StringTools;


import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.ICommand;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.ModLoader;

public class CommandAlias extends CommandBase {

	private ScriptEnvironment env;
	private String name;
	private CommandRunScript runCommand;
	
	public CommandAlias(ScriptEnvironment env, String name) {
		this.env = env;
		this.name = name;
		runCommand = env.modInst.getRunCommand();
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {		
		String command = StringTools.insertArgs(env.aliases.getAlias(name), args);
		if (command.startsWith("/")) {
			CommandHandler handler;
			if (MinecraftServer.getServer().getCommandManager() instanceof CommandHandler) {
				handler = (CommandHandler)(MinecraftServer.getServer().getCommandManager());
			}
			else {
				env.chat.err("An unexpected error has occured, I can't run the command, sorry.");
				G.LOG.warning("Command manager is not an instance of CommandHandler, dunno what to do!");			
				return;
			}
			handler.executeCommand(sender, command);			
		}
		else {		
			String[] newargs = command.split(" ");
			System.out.println(command);
			runCommand.processCommand(sender, newargs);
		}
	}
}
