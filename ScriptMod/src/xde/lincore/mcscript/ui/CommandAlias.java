package xde.lincore.mcscript.ui;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.ICommandSender;
import xde.lincore.mcscript.G;
import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.util.StringTools;

public class CommandAlias extends CommandBase {

	private final ScriptEnvironment env;
	private final String name;
	private final CommandRunScript runCommand;

	public CommandAlias(final ScriptEnvironment env, final String name) {
		this.env = env;
		this.name = name;
		runCommand = env.modInst.getRunCommand();
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public void processCommand(final ICommandSender sender, final String[] args) {
		final String command = StringTools.insertArgs(env.aliases.getAlias(name), args);
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
			final String[] newargs = command.split(" ");
			System.out.println(command);
			runCommand.processCommand(sender, newargs);
		}
	}
}
