package xde.lincore.mcscript.minecraft;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommandSender;
import xde.lincore.mcscript.env.G;
import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.mcscript.ui.BadUserInputException;
import xde.lincore.mcscript.ui.Keys;
import xde.lincore.mcscript.ui.Keywords;
import xde.lincore.util.Config;
import xde.lincore.util.StringTools;
import xde.lincore.util.Text;

public final class CommandScriptEnv extends CommandBase {

	/*
	 * Suggestion:
	 * Break up the class into pluggable "Commandlets" that do the actual work.
	 * Commandlets can subscribe to keyword "Paths", like /env cfg set and are called
	 * when the path matches their criteria.
	 * Don't know if it's worth the trouble, though.
	 * ~lincore81
	 */


	private final ScriptEnvironment env;
	private final Commands commands;
	private ICommandSender sender;
	private Deque<String> tokens;
	

	public CommandScriptEnv(final ScriptEnvironment env, final Commands commands) {
		this.env = env;
		this.commands = commands;
	}

	@Override
	public String getCommandName() {
		return G.CMD_SCRIPT_ENV;
	}

	@Override
	public String getCommandUsage(final ICommandSender par1iCommandSender) {
		// TODO /env: getCommandUsage
		return super.getCommandUsage(par1iCommandSender);
	}

	@Override
	public void processCommand(ICommandSender sender, final String[] arguments) {

		this.sender = sender;
		tokens = new ArrayDeque<String>(Arrays.asList(arguments));

		try {
			assertArgCount(1, "Error, at least one argument expected.");
			final String token = tokens.pollFirst();
			final Keywords keyword = Keywords.findMatch(token);
			switch(keyword) {
				case Info:
					handleInfo();
					break;
				case Alias:
					handleAliases();
					break;
				case Help:
					// TODO handle help keyword
					break;
				case Kill:
					doKillThread();
					break;
				case Files:
					handleFiles();
					break;
				case Config:
					handleConfig();
					break;
				case Key:
					handleKeys();
					break;
				default:
					throw new BadUserInputException("Invalid argument: " + token);
			}
		}
		catch (final BadUserInputException e) {
			env.chat.err(e.getMessage());
		}


		sender = null;
		tokens = null;
	}

	private void handleKeys() {
		assertArgCount(1, "Missing argument: either get, set, find, list or remove.");
		final String token = tokens.pollFirst();
		switch (Keywords.findMatch(token)) {
			case Get:
				doKeyGet();
				break;
			case List:
				doKeyList();
				break;
			case Set:
				doKeySet();
				break;
			case Remove:
				doKeyRemove();
				break;
			case Find:
				doKeyFind();
				break;
			default:
				throw new BadUserInputException("Invalid argument: " + token);
		}
	}

	private void handleAliases() {
		assertArgCount(1, "Missing Argument: either list, set or remove.");
		final String token = tokens.pollFirst();
		switch (Keywords.findMatch(token)) {
			case Set:
				doAliasSet();
				break;
			case Remove:
				doAliasRemove();
				break;
			case List:
				doAliasList();
				break;
			default:
				throw new BadUserInputException("Invalid argument: " + token);
		}
	}

	private void handleConfig() {
		assertArgCount(1, "Missing argument: either get, set, list, remove, save, reload or reset.");
		final String token = tokens.pollFirst();
		switch (Keywords.findMatch(token)) {
			case List:
				doConfigList();
				break;
			case Save:
				doConfigSave();
				break;
			case Reload:
				doConfigReload();
				break;
			case Reset:
				doConfigReset();
				break;
			case Set:
				doConfigSet();
				break;
			case Get:
				doConfigGet();
				break;
			case Remove:
				doConfigRemove();
				break;
			default:
				throw new BadUserInputException("Invalid argument: " + token);
		}
	}

	private void handleFiles() {
		assertArgCount(1, "Missing argument: either ls, cat, cd, cwd, mkdir, mv, rm, mgr or edit");
		final String token = tokens.pollFirst();
		switch (Keywords.findMatch(token)) {
			case Editor:
				doOpenTextEditor();
				break;
			case Manager:
				doStartFileManager();
				break;
			case List:
				doFilesList();
				break;
			case Cwd:
				env.chat.echo(env.files.getCwdString());
				break;
			case Cat:
				doFilesCat();
				break;
			default:
				throw new BadUserInputException("Invalid argument: " + token);
		}
	}


	private void handleInfo() {
		assertArgCount(1, "Missing argument");
		final String token = tokens.pollFirst();
		switch (Keywords.findMatch(token)) {
			case Engines:
				doInfoEnginesList();
				break;
			case Engine:
				doInfoEngineShow();
				break;
			case Threads:
				doInfoThreadsList();
				break;
			case Key:
				if (tokens.size() > 0) {
					doInfoAproposKeys();
				} else {
					doInfoDumpKeys();
				}
				break;
			default:
				if (token.startsWith("$")) {
					doInfoShowFile(token.substring(1));
				}
				else {
					throw new BadUserInputException("Invalid argument: " + token);
				}
		}
	}


	private void doAliasList() {
		env.chat.echo("§oRegistered Aliases:");
		final Map<Object, Object> sorted = env.aliases.getSortedMap();
		for (final Map.Entry entry: sorted.entrySet()) {
			String substr = (String)(entry.getValue());
			if (substr.length() > 60) {
				substr = substr.substring(0, 58) + "...";
			}
			env.chat.echo("§e* " + (String)(entry.getKey()) + ":§r " + substr);
		}
		if (env.aliases.getAliases().isEmpty()) {
			env.chat.echo("     - none -");
		}
	}


	private void doAliasRemove() {
		assertArgCount(1, "Which alias would you like to remove?");
		final String name = tokens.pollFirst();
		if (env.aliases.removeAlias(name)) {
			env.chat.echo("Ok.");
		} else {
			env.chat.err("The alias \"" + name + "\" doesn't exist.");
		}
	}


	private void doAliasSet() {
		assertArgCount(1, "Missing argument.");
		final String token = StringTools.join(tokens).trim();
		final String[] pair = token.split("\\s*=\\s*", 2);
		if (pair.length != 2) {
			throw new BadUserInputException("Bad syntax. Write <KEY>'='<VALUE>.");
		}
		env.aliases.setAlias(pair[0], pair[1]);
		env.chat.echo("Ok.");
	}


	private void doConfigGet() {
		assertArgCount(1, "Please enter the name of the property you want to get.");

		final String propName = StringTools.join(tokens, " ");
		final String propValue = Config.get(G.CFG_MAIN, propName);
		if (propValue != null) {
			env.chat.echo(propValue);
		}
		else {
			env.chat.echo("\"" + propName + "\" doesn't exist.");
		}
	}


	private void doConfigList() {
		String cfg;
		if (tokens.isEmpty()) {
			cfg = G.CFG_MAIN;
			env.chat.echo("§oMod Config:");
		}
		else {
			cfg = tokens.pop();
			if (Config.getMap(cfg.toLowerCase()) == null) {
				throw new BadUserInputException("The config table \"" + cfg + "\" doesn't exist.");
			}
			env.chat.echo("§oConfig table \"" + cfg + "\":");
		}

		final Map<Object, Object> sorted = Config.getSortedMap(cfg);
		for (final Map.Entry entry: sorted.entrySet()) {
			final String key = (String)(entry.getKey());
			final String value = (String)(entry.getValue());
			env.chat.echo("    \"§e" + key + "§r\" = \"§e" + value + "§r\"");
		}

	}


	private void doConfigReload() {
		final boolean success = Config.load(G.CFG_MAIN);
		env.chat.echo(success? "Ok." : "Properties were NOT reloaded.");

	}


	private void doConfigRemove() {
		assertArgCount(1, "Please enter the name of the property you want to remove.");

		final String propName = StringTools.join(tokens, " ");
		if (Config.remove(G.CFG_MAIN, propName)) {
			env.chat.echo("Ok.");
			if (!Config.autosave(G.CFG_MAIN)) {
				env.chat.err("Autosave failed!");
			}
		}
		else {
			env.chat.echo("There is no property by the name of \"" + propName + "\".\n" +
					"Nothing has been removed.");
		}
	}


	private void doConfigReset() {
		Config.clearMap(G.CFG_MAIN, G.defaultProperties);
		if (!Config.autosave(G.CFG_MAIN)) {
			env.chat.echo("§c§oAutosave failed!");
		}
	}


	private void doConfigSave() {
		final boolean success = Config.save(G.CFG_MAIN);
		env.chat.echo(success? "Ok." : "Properties were NOT saved.");
	}


	private void doConfigSet() {
		assertArgCount(1, "Missing argument.");
		final String mapString = StringTools.join(tokens, " ");

		if (!StringTools.isValidMap(mapString, ",", "=")) {
			throw new BadUserInputException("Please set properties like this:\n" +
					"§f<name>§r = §f<value>§r (; §f<name2>§r = §f<value2>§r; ... " +
					"§f<nameN>§r = §f<valueN>)§r");
		}
		final Map<String, String> newProps = StringTools.getMap(mapString, ",", "=");
		if (newProps == null) {
			throw new BadUserInputException("Please set properties like this:\n" +
					"§o§f<name> = <value> (; <name2>§r = <value2>; ... " +
					"<nameN> = <valueN>)");
		}
		else {
			Config.setMultiple(G.CFG_MAIN, newProps);
			env.chat.echo("Ok.");
			if (!Config.autosave(G.CFG_MAIN)) {
				env.chat.echo("§c§oAutosave failed!");
			}
		}
	}

	/**
	 * Print a text file on screen.
	 */
	private void doFilesCat() {
		assertArgCount(1, "Not enough arguments. Please add the name of the file you wish to read.");
		String filename = StringTools.join(tokens);
		if (filename.startsWith("$")) {
			filename = filename.substring(1);
		}
		File file;
		try {
			file = env.files.resolvePath(filename);
		}
		catch (final IOException e) {
			e.printStackTrace();
			env.chat.err("Could not resolve path: " + filename);
			return;
		}

		final String charset = Config.get(G.CFG_MAIN, G.PROP_ENCODING);
		final Text contents = new Text();
		try {
			contents.readFile(file, charset);
		} catch (final FileNotFoundException e) {
			throw new BadUserInputException("File not found: " + file.toString());
		} catch (final IOException e) {
			e.printStackTrace();
			throw new BadUserInputException("Could not read the file " + file.toString(), e);
		} catch (final IllegalArgumentException e) {
			Config.remove(G.CFG_MAIN, G.PROP_ENCODING);
			throw new BadUserInputException("Invalid or unsupported file encoding: \"" +
					charset + "\". " + G.PROP_ENCODING +
					" has been reset to " + Text.DEFAULT_CHARSET.name());
		}


		contents.replaceAll("\\t", "    "); // mc can't handle tabs.
		contents.replaceAll("§([0-9a-fA-F])", "<$\1>"); // avoid color codes

		int i = 1;
		final int w = String.valueOf(contents.getLineCount()).length();
		for (final String line : contents.lines()) {
			env.chat.format("§e%0" + String.valueOf(w) + "d:§r %s", i, line);
			i++;
		}
	}

	private void doFilesList() {
		final String dirParam = StringTools.join(tokens);
		File dir;
		try {
			dir = env.files.resolvePath(dirParam);
		}
		catch (final IOException e) {
			e.printStackTrace();
			env.chat.err("Could not resolve path: " + dirParam);
			return;
		}

		try {
			env.chat.echo("§o" + dir.getCanonicalPath() + ":");
		} catch (final IOException e) {
			e.printStackTrace();
			G.LOG.warning("Could not get canonical path for " + dir.getAbsolutePath());
			env.chat.echo("§o" + dir.getAbsolutePath() + ":");
		}

		final File[] contents = dir.listFiles();
		if (contents == null) {
			throw new BadUserInputException("Is not a valid directory: " + dir.getAbsolutePath());
		}
		for (final File entry: contents) {
			if (entry.isDirectory()) {
				env.chat.echo("  §9" + entry.getName() + System.getProperty("file.separator", "/"));
			}
			else {
				env.chat.echo("  " + entry.getName());
			}
		}
	}

	private void doInfoAproposKeys() {
		env.chat.echo("§oKeys:");
		while (tokens.size() > 0) {
			for (final Keys k: Keys.apropos(tokens.pollFirst())) {
				env.chat.echo(k);
			}
		}
	}

	private void doInfoDumpKeys() {
		env.chat.echo("§oKeys:");
		env.chat.echo(Keys.getDump());
	}

	/** Show more detailed information about an available script engine.
	 */
	private void doInfoEngineShow() {
		assertArgCount(1, "Missing argument.");

		final String engineName = tokens.pollFirst();
		ScriptEngine engine;
		try {
			if (Keywords.Default.matches(engineName)) {
				env.chat.echo("§oThe default script engine is:");
				engine = env.engines.getDefaultEngine();
			}
			else if (Keywords.Current.matches(engineName)) {
				env.chat.echo("§oThe currently used script engine is:");
				engine = env.engines.getCurrentEngine();
			}
			else {
				engine = env.engines.getEngine(engineName);
			}
		} catch (final IllegalArgumentException e) {
			throw new BadUserInputException(e);
		}

		if (engine == null) {
			throw new BadUserInputException("There is no script engine with such a name.");
		}
		else {
			final ScriptEngineFactory fac = engine.getFactory();
			final String mimes = StringTools.join(fac.getMimeTypes(), ", ");
			final String compilable = (engine instanceof Compilable) ? "yes" : "no";
			final String invocable = (engine instanceof Invocable) ? "yes" : "no";


			doInfoShowEngine(fac);
			env.chat.echo("  Mimes: " + mimes);
			env.chat.echo("  Compilable? " + compilable);
			env.chat.echo("  Invocable? " + compilable);
		}
	}


	private void doInfoEnginesList() {
		final ScriptEngineManager mgr = env.engines.getManager();
		final List<ScriptEngineFactory> facs = mgr.getEngineFactories();
		env.chat.echo("§oAvailable script engines:");
		for (final ScriptEngineFactory f: facs) {
			doInfoShowEngine(f);
		}
	}


	private void doInfoShowEngine(final ScriptEngineFactory factory) {
		final String extensions = StringTools.join(factory.getExtensions(), ", ");
		final String aliases = StringTools.join(factory.getNames(), ", ");
		env.chat.echo("§e* " + factory.getEngineName() + " for " + factory.getLanguageName() + " " +
				factory.getLanguageVersion());
		env.chat.echo("  Aliases: " + aliases);
		env.chat.echo("  Extensions: " + extensions);
	}


	private void doInfoShowFile(final String filename) {
		final File file = new File(G.DIR_MOD, filename);
		final Text contents = new Text();
		try {
			contents.readFile(file);
		} catch (final FileNotFoundException e) {
			env.chat.echo("File not found: " + file.toString());
			return;
		} catch (final IOException e) {
			env.chat.echo("An error occured while trying to access the file " + file.toString());
			env.chat.echo(e.getMessage());
			e.printStackTrace();
			return;
		}
		final ScriptEngine engine = env.engines.getManager().getEngineByExtension(
				env.files.getFileExtension(file.toString()));
		final String lang = (engine != null) ? engine.getFactory().getLanguageName() : "unknown";

		env.chat.echo("§o" + file.getAbsolutePath() + ":");
		env.chat.echo("§eFile Size: §r" + String.valueOf(file.length()));
		env.chat.echo("§eLines: §r" + String.valueOf(contents.getLineCount()));
		env.chat.echo("§eLine Terminator: §r" + contents.getFancyLineTerminator());
		env.chat.echo("§eScript Language: §r" + lang);
	}


	private void doInfoThreadsList() {
		final ThreadGroup grp = Thread.currentThread().getThreadGroup();
		final int threadCount = grp.activeCount();
		final Thread[] threads = new Thread[threadCount];
		grp.enumerate(threads);
		env.chat.echo("§oActive Threads:");
		for (int i = 0; i < threadCount; i++) {
			final Thread t = threads[i];
			env.chat.echo(String.format("§e%s:§r  id=%d pri=%d", t.getName(), t.getId(), t.getPriority()));
		}
	}


	private void doKeyFind() {
		assertArgCount(1, "Missing argument: key");
		final String keyStr = StringTools.join(tokens).trim();
		final Keys key = Keys.find(keyStr);
		if (key != null) {
			env.chat.echo("§oKey found:§r\n" + key.toString());
		} else {
			env.chat.echo("§oThere is no such key.");
		}
	}


	private void doKeyGet() {
		assertArgCount(1, "Missing argument: key");
		final String keyStr = StringTools.join(tokens).trim();
		dumpKeyBinding(keyStr);
	}

	private void doKeyList() {
		final Map keymap = Config.getSortedMap(G.CFG_KEYS);
		for (final Object obj: keymap.keySet()) {
			dumpKeyBinding((String)obj);
		}
	}

	private void doKeyRemove() {
		assertArgCount(1, "Missing argument: key");
		final String token = StringTools.join(tokens).trim();
		if (env.keys.isGameKey(token)) {
			throw new BadUserInputException("That key was bound by the game and is probably " +
					"important. I wouldn't dare to remove it.");
		}
		final boolean success = env.keys.removeKey(token);
		if (!success) {
			env.chat.err("Could not remove the key " + Keys.find(token) + ". Maybe you never bound it?");
		}
		else {
			env.keys.saveConfig();
			env.chat.echo("Ok.");
		}
	}

	private void doKeySet() {
		assertArgCount(1, "Missing argument: key");
		final String token = StringTools.join(tokens).trim();
		final String[] pair = token.split("\\s*=\\s*", 2);
		if (pair.length != 2) {
			throw new BadUserInputException("Bad syntax. Write <KEY>'='<VALUE>.");
		}
		if (env.keys.isGameKey(pair[0])) {
			throw new BadUserInputException("That key was bound by the game and is probably " +
					"important. Rebind it in the menu, then try again.");
		}
		if (env.keys.setKey(pair[0], pair[1])) {
			env.keys.saveConfig();
			env.chat.echo("Ok.");
		} else {
			env.chat.err("Could not set key: " + pair[0] + ".");
		}
	}


	private void doKillThread() {
		assertArgCount(1, "Missing argument: thread id.");
		Integer threadId;
		final String idStr = tokens.pop();
		threadId = StringTools.getInteger(idStr);

		if (threadId == null) {
			env.chat.echo("Bad argument: \"" + idStr + "\". " +
					"Please specify the id of the thread you want to kill.");
			return;
		}

		final boolean success = env.stopThread(threadId);
		if (success) {
			env.chat.echo("Ok.");
		}
		else {
			env.chat.echo("Could not kill the thread with id=\"" + idStr + "\".");
		}
	}

	private void doStartFileManager() {
		final String dirParam = StringTools.join(tokens);
		File dir = null;
		try {
			dir = env.files.resolvePath(dirParam);
		} catch (final IOException e1) {
			env.chat.err("Could not resolve path: " + dirParam);
			e1.printStackTrace();
			return;
		}

		if (!dir.isDirectory()) {
			env.chat.err("Can't do: \"" + dir + "\" is not a directory.");
			return;
		}
		final String filemanager = Config.get(G.CFG_MAIN, G.PROP_TOOL_FILEMGR);
		if (filemanager != null) {
			try {
				if (filemanager.equalsIgnoreCase("auto")) {
					Desktop.getDesktop().open(dir);
				}
				else {
					final String cmd = filemanager + " " + dir.getCanonicalPath();
					Runtime.getRuntime().exec(new String[]{filemanager, dir.getCanonicalPath()});
				}
			} catch (final IOException e) {
				env.chat.err("Could not start file manager: " + filemanager +
						"\n" + e.getMessage());
				e.printStackTrace();
			}
		}
		else {
			env.chat.err("No file manager set. Please set " + G.PROP_TOOL_FILEMGR + " first.");
		}
	}

	private void doOpenTextEditor() {
		final String dirParam = StringTools.join(tokens);
		File dir = null;
		try {
			dir = env.files.resolvePath(dirParam);
		} catch (final IOException e1) {
			env.chat.err("Could not resolve path: " + dirParam);
			e1.printStackTrace();
			return;
		}

		if (dir.isDirectory()) {
			env.chat.err("Can't do: \"" + dir + "\" is not a file.");
			return;
		}
		final String editor = Config.get(G.CFG_MAIN, G.PROP_TOOL_EDITOR);
		if (editor != null) {
			try {
				if (editor.equalsIgnoreCase("auto")) {
					Desktop.getDesktop().open(dir);
				}
				else {
					final String cmd = editor + " " + dir.getCanonicalPath();
					Runtime.getRuntime().exec(new String[]{editor, dir.getCanonicalPath()});
				}
			} catch (final IOException e) {
				env.chat.err("Could not start text editor: " + editor +
						"\n" + e.getMessage());
				e.printStackTrace();
			}
		}
		else {
			env.chat.err("No text editor set. Please set " + G.PROP_TOOL_EDITOR + " first.");
		}
	}


	private void dumpKeyBinding(final String keyStr) {
		final Keys key = Keys.find(keyStr);
		final String action = env.keys.getAction(key);
		if (key == null) {
			env.chat.err(keyStr + ": There is no such key.");
		}
		else if (action == null) {
			env.chat.err(key.name + " is not bound.");
		}
		else {
			env.chat.format("§e%s§r: %s", key.name, action);
		}
	}

	private void assertArgCount(final int size, final String errorMsg) {
		if (tokens.size() < size) {
			throw new BadUserInputException(errorMsg);
		}
	}
}
