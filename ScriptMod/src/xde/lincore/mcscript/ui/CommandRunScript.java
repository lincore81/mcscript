package xde.lincore.mcscript.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommandSender;
import xde.lincore.mcscript.G;
import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.util.Config;
import xde.lincore.util.StringTools;
import xde.lincore.util.Text;



public final class CommandRunScript extends CommandBase {

	private final ScriptEnvironment env;

	private static final int RE_GRP_LANG = 2;
	private static final int RE_GRP_FILE = 3;
	private static final int RE_GRP_ARGS = 4;
	private static final int RE_GRP_EXPR = 5;

	private static final String OP_DELIMITER = ",";
	private static final String OP_ASSIGNMENT = "=";


	public CommandRunScript(final ScriptEnvironment env) {
		this.env = env;
	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender sender) {
		return MinecraftServer.getServer().isSinglePlayer();
	}


	@Override
	public List getCommandAliases() {
		return Arrays.asList(new String[]{"."});
	}

	@Override
	public String getCommandName() {
		return G.CMD_RUN_SCRIPT;
	}

	@Override
	public String getCommandUsage(final ICommandSender par1iCommandSender) {
		final String run = getCommandName();
		final String result =
				"§f\n    /" + run + " ['-'<engine>] <expression>\n" +
				"    /" + run + " ['-'<engine>] '$'<file> [':' <arguments>]\n";
		return result;
	}

	@Override
	public void processCommand(final ICommandSender sender, final String[] tokens) {
		String langParam = null;
		String fileParam = null;
		String argsParam = null;
		String exprParam = null;
		final String engine = null;
		final String input = joinString(tokens, 0);
		final String regex = "\\s*(-([A-Za-z0-9_]+))?\\s*(?:\\$([^:]+)\\s*(?:\\:\\s*(.*))?|([^$].+))";

		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(input);

		if (m.matches()) {
			langParam = m.group(RE_GRP_LANG);
			fileParam = m.group(RE_GRP_FILE);
			argsParam = m.group(RE_GRP_ARGS);
			exprParam = m.group(RE_GRP_EXPR);

			if (langParam != null) {
				langParam = langParam.trim();
			}
			if (fileParam != null) {
				fileParam = fileParam.trim();
			}
			final boolean success = runScript(langParam, fileParam, argsParam, exprParam);
			if (success) {
				return;
			}
		}
		env.chat.err("Syntax error:§r\n\t" + input);
	}

	private boolean runScript(final String language, final String filename, final String argsList, final String source) {
		if (source != null) {
			env.runScript(source, language, null);
			return true;
		}
		else if (filename != null) { // do file
			Map<String, String> arguments = null;
			if (argsList != null && StringTools.isValidMap(argsList, OP_DELIMITER, OP_ASSIGNMENT)) {
				arguments = StringTools.getMap(argsList, OP_DELIMITER, OP_ASSIGNMENT);
				arguments.put("0", filename);
			}
			try {
				env.runScriptFile(filename, language, arguments);
			} catch (final FileNotFoundException e) {
				env.chat.err("File not found: " + filename);
				e.printStackTrace();
			} catch (final IOException e) {
				env.chat.err("An error occured while trying to access the file " + filename + ":");
				env.chat.echo(e.getMessage());
				e.printStackTrace();
			} catch (final IllegalArgumentException e) {
				handleCharsetExceptions(e);
			}
			return true;
		}
		return false;
	}

	private void handleCharsetExceptions(final IllegalArgumentException e) {
		if (e instanceof IllegalCharsetNameException || e instanceof UnsupportedCharsetException) {
			final String removeCommandSyntaxString = G.CMD_PREFIX + G.CMD_SCRIPT_ENV + " " +
					Keywords.getPathString(Keywords.Config, Keywords.Remove) + " " +
					Config.get(G.CFG_MAIN, G.PROP_ENCODING);

			env.chat.err("The file encoding \"" + Config.get(G.CFG_MAIN, G.PROP_ENCODING) +
					"\" specified in " + G.PROP_ENCODING + " is either" +
					" not a valid charset name or unsupported on this platform.");

			env.chat.echo("Type\n\t§a" + removeCommandSyntaxString + "\n§rto restore " +
					"the default setting (" + Text.DEFAULT_CHARSET.displayName() + ")");
		}
		else {
			throw e;
		}
	}
}
