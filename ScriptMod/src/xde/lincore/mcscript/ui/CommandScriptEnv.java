package xde.lincore.mcscript.ui;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.bouncycastle.util.Strings;

import xde.lincore.mcscript.G;
import xde.lincore.mcscript.ScriptingEnvironment;
import xde.lincore.mcscript.exception.BadUserInput;
import xde.lincore.mcscript.wrapper.MinecraftWrapper;
import xde.lincore.util.Config;
import xde.lincore.util.StringTools;
import xde.lincore.util.Text;


import net.minecraft.client.Minecraft;
import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.ModLoader;
import net.minecraft.src.StringUtils;
import net.minecraft.src.mod_McScript;

public final class CommandScriptEnv extends CommandBase {

	/*
	 * TODO 	Break up the class into pluggable "Commandlets" that do the actual work.
	 * 			Commandlets can subscribe to keyword "Paths", like /env cfg set and are called
	 * 			when the path matches their criteria.
	 */
	
	
	private ScriptingEnvironment env;
	private mod_McScript modInst;
	
	private ICommandSender sender;
	private MinecraftWrapper mc;
	private Deque<String> tokens;
	
	public CommandScriptEnv(mod_McScript modInst, ScriptingEnvironment env) {
		this.env = env;
		this.modInst = modInst;
	}
	
	@Override
	public String getCommandName() {
		return "env";
	}

	@Override
	public String getCommandUsage(ICommandSender par1iCommandSender) {
		// TODO /env: getCommandUsage
		return super.getCommandUsage(par1iCommandSender);
	}

	@Override
	public void processCommand(ICommandSender sender, String[] arguments) {		
		this.mc = env.getMc();
		this.sender = sender;
		this.tokens = new ArrayDeque<String>(Arrays.asList(arguments));
		
		try {
			assertArgCount(1, "Error, at least one argument expected.");			
			String token = tokens.pollFirst();
			Keywords keyword = Keywords.findMatch(token);
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
					throw new BadUserInput("Invalid argument: " + token);
			}
		}
		catch (BadUserInput e) {
			mc.err(e.getMessage());
		}
		
		mc = null;
		sender = null;
		tokens = null;
	}

	private void handleKeys() {
		assertArgCount(1, "Missing argument: either get, set, find, list or remove.");
		String token = tokens.pollFirst();
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
				throw new BadUserInput("Invalid argument: " + token);
		}
	}

	private void handleAliases() {
		assertArgCount(1, "Missing Argument: either list, set or remove.");
		String token = tokens.pollFirst();
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
				throw new BadUserInput("Invalid argument: " + token);
		}
	}

	private void handleConfig() {
		assertArgCount(1, "Missing argument: either get, set, list, remove, save, reload or reset.");
		String token = tokens.pollFirst();
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
				throw new BadUserInput("Invalid argument: " + token);
		}
	}

	private void handleFiles() {		
		assertArgCount(1, "Missing argument: either ls, cat, cd, cwd, mkdir, mv, rm, mgr or edit");		
		String token = tokens.pollFirst();		
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
				mc.echo(env.files.getCwdString());
				break;			
			case Cat:
				doFilesCat();
				break;
			default:
				throw new BadUserInput("Invalid argument: " + token);
		}
	}
	

	private void handleInfo() {
		assertArgCount(1, "Missing argument");
		String token = tokens.pollFirst();
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
					throw new BadUserInput("Invalid argument: " + token);
				}
		}
	}

	
	private void doAliasList() {
		mc.echo("§oRegistered Aliases:");
		Map<Object, Object> sorted = env.aliases.getSortedMap();
		for (Map.Entry entry: sorted.entrySet()) {
			String substr = (String)(entry.getValue());
			if (substr.length() > 60) {
				substr = substr.substring(0, 58) + "...";
			}
			mc.echo("§e* " + (String)(entry.getKey()) + ":§r " + substr); 
		}
		if (env.aliases.getAliases().isEmpty()) {
			mc.echo("     - none -");
		}
	}

	
	private void doAliasRemove() {
		assertArgCount(1, "Which alias would you like to remove?");
		String name = tokens.pollFirst();
		env.aliases.removeAlias(name);		
	}
	
	
	private void doAliasSet() {
		assertArgCount(1, "Missing argument.");
		String token = StringTools.join(tokens).trim();
		String[] pair = token.split("\\s*=\\s*", 2);		
		if (pair.length != 2) {
			throw new BadUserInput("Bad syntax. Write <KEY>'='<VALUE>.");
		}
		env.aliases.setAlias(pair[0], pair[1]);
		mc.echo("Ok.");
	}
	

	private void doConfigGet() {
		assertArgCount(1, "Please enter the name of the property you want to get.");

		String propName = StringTools.join(tokens, " ");
		String propValue = Config.get(G.CFG_MAIN, propName);
		if (propValue != null) {
			mc.echo(propValue);
		}
		else {
			mc.echo("\"" + propName + "\" doesn't exist.");
		}		
	}

	
	private void doConfigList() {
		String cfg;
		if (tokens.isEmpty()) {
			cfg = G.CFG_MAIN;
			mc.echo("§oMod Config:");
		}
		else {
			cfg = tokens.pop();
			if (Config.getMap(cfg.toLowerCase()) == null) {
				throw new BadUserInput("The config table \"" + cfg + "\" doesn't exist.");
			}
			mc.echo("§oConfig table \"" + cfg + "\":");
		}
		
		Map<Object, Object> sorted = Config.getSortedMap(cfg);
		for (Map.Entry entry: sorted.entrySet()) {
			String key = (String)(entry.getKey());
			String value = (String)(entry.getValue());
			mc.echo("    \"§e" + key + "§r\" = \"§e" + value + "§r\"");
		}
		
	}
	

	private void doConfigReload() {
		boolean success = Config.load(G.CFG_MAIN);
		mc.echo(success? "Ok." : "Properties were NOT reloaded.");
		
	}
	

	private void doConfigRemove() {
		assertArgCount(1, "Please enter the name of the property you want to remove.");

		String propName = StringTools.join(tokens, " ");			
		if (Config.remove(G.CFG_MAIN, propName)) {
			mc.echo("Ok.");
			if (!Config.autosave(G.CFG_MAIN)) mc.err("Autosave failed!");
		}
		else {
			mc.echo("There is no property by the name of \"" + propName + "\".\n" +
					"Nothing has been removed.");
		}		
	}

	
	private void doConfigReset() {
		Config.clearMap(G.CFG_MAIN, modInst.setupDefaultProperties());	
		if (!Config.autosave(G.CFG_MAIN)) mc.echo("§c§oAutosave failed!");		
	}
	

	private void doConfigSave() {
		boolean success = Config.save(G.CFG_MAIN);
		mc.echo(success? "Ok." : "Properties were NOT saved.");		
	}

	
	private void doConfigSet() {
		assertArgCount(1, "Missing argument.");
		String mapString = StringTools.join(tokens, " ");
		
		if (!StringTools.isValidMap(mapString, ",", "=")) {
			throw new BadUserInput("Please set properties like this:\n" +
					"§f<name>§r = §f<value>§r (; §f<name2>§r = §f<value2>§r; ... " +
					"§f<nameN>§r = §f<valueN>)§r");
		}
		Map<String, String> newProps = StringTools.getMap(mapString, ",", "=");
		if (newProps == null) {
			throw new BadUserInput("Please set properties like this:\n" +
					"§o§f<name> = <value> (; <name2>§r = <value2>; ... " +
					"<nameN> = <valueN>)");
		}
		else {
			Config.setMultiple(G.CFG_MAIN, newProps);			
			mc.echo("Ok.");
			if (!Config.autosave(G.CFG_MAIN)) mc.echo("§c§oAutosave failed!");
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
		catch (IOException e) {
			e.printStackTrace();
			mc.err("Could not resolve path: " + filename);
			return;
		}
		
		String charset = Config.get(G.CFG_MAIN, G.PROP_ENCODING);
		Text contents = new Text();
		try {
			contents.readFile(file, charset);
		} catch (FileNotFoundException e) {
			throw new BadUserInput("File not found: " + file.toString());
		} catch (IOException e) {
			e.printStackTrace();
			throw new BadUserInput("Could not read the file " + file.toString(), e);
		} catch (IllegalArgumentException e) {
			Config.remove(G.CFG_MAIN, G.PROP_ENCODING);
			throw new BadUserInput("Invalid or unsupported file encoding: \"" + 
					charset + "\". " + G.PROP_ENCODING + 
					" has been reset to " + Text.DEFAULT_CHARSET.name());
		}
		
		
		contents.replaceAll("\\t", "    "); // mc can't handle tabs.
		contents.replaceAll("§([0-9a-fA-F])", "<$\1>"); // avoid color codes
		
		int i = 1;
		int w = String.valueOf(contents.getLineCount()).length();
		for (String line : contents.lines()) {
			mc.format("§e%0" + String.valueOf(w) + "d:§r %s", i, line);
			i++;
		}
	}

	private void doFilesList() {
		String dirParam = StringTools.join(tokens);
		File dir;
		try {
			dir = env.files.resolvePath(dirParam);
		}
		catch (IOException e) {
			e.printStackTrace();
			mc.err("Could not resolve path: " + dirParam);
			return;
		}
		
		try {
			mc.echo("§o" + dir.getCanonicalPath() + ":");			
		} catch (IOException e) {
			e.printStackTrace();
			G.LOG.warning("Could not get canonical path for " + dir.getAbsolutePath());
			mc.echo("§o" + dir.getAbsolutePath() + ":");
		}	
		
		File[] contents = dir.listFiles();		
		if (contents == null) {
			throw new BadUserInput("Is not a valid directory: " + dir.getAbsolutePath());
		}
		for (File entry: contents) {
			if (entry.isDirectory()) {
				mc.echo("  §9" + entry.getName() + System.getProperty("file.separator", "/"));
			}
			else {
				mc.echo("  " + entry.getName());
			}
		}
	}

	private void doInfoAproposKeys() {
		mc.echo("§oKeys:");
		while (tokens.size() > 0) {
			for (Keys k: Keys.apropos(tokens.pollFirst())) {
				mc.echo(k);				
			}
		}
	}
	
	private void doInfoDumpKeys() {
		mc.echo("§oKeys:");
		mc.echo(Keys.getDump());
	}

	/** Show more detailed information about an available script engine.
	 */
	private void doInfoEngineShow() {
		assertArgCount(1, "Missing argument.");

		ScriptEngineManager mgr = env.getManager();
		String engineName = tokens.pollFirst();
		ScriptEngine engine;
		if (Keywords.Default.matches(engineName)) {			
			mc.echo("§oThe default script engine is:");
			engine = env.getDefaultEngine();
		}
		else if (Keywords.Current.matches(engineName)) {			
			mc.echo("§oThe currently used script engine is:");
			engine = env.getCurrentEngine();
		}
		else {
			engine = env.findEngine(engineName);
		}
		
		if (engine == null) {
			throw new BadUserInput("There is no script engine with such a name.");
		}
		else {
			ScriptEngineFactory fac = engine.getFactory();
			String mimes = StringTools.join(fac.getMimeTypes(), ", ");
			String compilable = (engine instanceof Compilable) ? "yes" : "no";
			String invocable = (engine instanceof Invocable) ? "yes" : "no";
			
			
			doInfoShowEngine(fac);
			mc.echo("  Mimes: " + mimes);
			mc.echo("  Compilable? " + compilable);
			mc.echo("  Invocable? " + compilable);
		}
	}

	
	private void doInfoEnginesList() {
		ScriptEngineManager mgr = env.getManager();
		List<ScriptEngineFactory> facs = mgr.getEngineFactories();			
		mc.echo("§oAvailable script engines:");
		for (ScriptEngineFactory f: facs) {				
			doInfoShowEngine(f);
		}
	}

	
	private void doInfoShowEngine(ScriptEngineFactory factory) {
		String extensions = StringTools.join(factory.getExtensions(), ", ");
		String aliases = StringTools.join(factory.getNames(), ", ");				
		mc.echo("§e* " + factory.getEngineName() + " for " + factory.getLanguageName() + " " + 
				factory.getLanguageVersion());
		mc.echo("  Aliases: " + aliases);
		mc.echo("  Extensions: " + extensions);
	}

	
	private void doInfoShowFile(String filename) {
		File file = new File(G.DIR_MOD, filename);
		Text contents = new Text();
		try {
			contents.readFile(file);
		} catch (FileNotFoundException e) {
			mc.echo("File not found: " + file.toString());
			return;
		} catch (IOException e) {
			mc.echo("An error occured while trying to access the file " + file.toString());
			mc.echo(e.getMessage());
			e.printStackTrace();
			return;
		}
		ScriptEngine engine = env.getManager().getEngineByExtension(
				env.files.getFileExtension(file.toString()));
		String lang = (engine != null) ? engine.getFactory().getLanguageName() : "unknown";
		
		mc.echo("§o" + file.getAbsolutePath() + ":");	
		mc.echo("§eFile Size: §r" + String.valueOf(file.length()));
		mc.echo("§eLines: §r" + String.valueOf(contents.getLineCount()));
		mc.echo("§eLine Terminator: §r" + contents.getFancyLineTerminator());
		mc.echo("§eScript Language: §r" + lang);
	}

	
	private void doInfoThreadsList() {
		ThreadGroup grp = Thread.currentThread().getThreadGroup();
		int threadCount = grp.activeCount();
		Thread[] threads = new Thread[threadCount];
		grp.enumerate(threads);
		mc.echo("§oActive Threads:");
		for (int i = 0; i < threadCount; i++) {			
			Thread t = threads[i];
			mc.echo(String.format("§e%s:§r  id=%d pri=%d", t.getName(), t.getId(), t.getPriority()));
		}
	}
	
	
	private void doKeyFind() {
		assertArgCount(1, "Missing argument: key");
		String keyStr = StringTools.join(tokens).trim();
		Keys key = Keys.find(keyStr);
		if (key != null) {
			mc.echo("§oKey found:§r\n" + key.toString());
		} else {
			mc.echo("§oThere is no such key.");
		}
	}
	
	
	private void doKeyGet() {
		assertArgCount(1, "Missing argument: key");
		String keyStr = StringTools.join(tokens).trim();
		dumpKeyBinding(keyStr);
	}
	
	private void doKeyList() {
		Map keymap = Config.getSortedMap(G.CFG_KEYS);
		for (Object obj: keymap.keySet()) {
			dumpKeyBinding((String)obj);
		}
	}
	
	private void doKeyRemove() {	
		assertArgCount(1, "Missing argument: key");
		String token = StringTools.join(tokens).trim();
		if (env.keys.isGameKey(token)) {
			throw new BadUserInput("That key was bound by the game and is probably " +
					"important. I wouldn't dare to remove it.");
		}
		boolean success = env.keys.removeKey(token);
		if (!success) {
			mc.err("Could not remove the key " + Keys.find(token) + ". Maybe you never bound it?");
		}
		else {
			env.keys.saveConfig();
			mc.echo("Ok.");
		}
	}	
	
	private void doKeySet() {
		assertArgCount(1, "Missing argument: key");
		String token = StringTools.join(tokens).trim();
		String[] pair = token.split("\\s*=\\s*", 2);		
		if (pair.length != 2) {
			throw new BadUserInput("Bad syntax. Write <KEY>'='<VALUE>.");
		}
		if (env.keys.isGameKey(pair[0])) {
			throw new BadUserInput("That key was bound by the game and is probably " +
					"important. Rebind it in the menu, then try again.");
		}
		if (env.keys.setKey(pair[0], pair[1])) {
			env.keys.saveConfig();
			mc.echo("Ok.");
		} else {
			mc.err("Could not set key: " + pair[0] + ".");
		}
	}
	
	
	private void doKillThread() {
		assertArgCount(1, "Missing argument: thread id.");
		Integer threadId;
		String idStr = tokens.pop();
		threadId = StringTools.getInteger(idStr);
		
		if (threadId == null) {
			mc.echo("Bad argument: \"" + idStr + "\". " +
					"Please specify the id of the thread you want to kill.");			
			return;
		}
		
		boolean success = env.stopThread(threadId);
		if (success) {
			mc.echo("Ok.");
		}
		else {
			mc.echo("Could not kill the thread with id=\"" + idStr + "\".");
		}
	}
	
	private void doStartFileManager() {
		String dirParam = StringTools.join(tokens);
		File dir = null;
		try {
			dir = env.files.resolvePath(dirParam);
		} catch (IOException e1) {			
			mc.err("Could not resolve path: " + dirParam);
			e1.printStackTrace();
			return;
		}
		 
		if (!dir.isDirectory()) {
			env.getMc().err("Can't do: \"" + dir + "\" is not a directory.");
			return;
		}
		String filemanager = Config.get(G.CFG_MAIN, G.PROP_TOOL_FILEMGR);
		if (filemanager != null) {	
			try {
				if (filemanager.equalsIgnoreCase("auto")) {
					Desktop.getDesktop().open(dir);
				}
				else {
					String cmd = filemanager + " " + dir.getCanonicalPath();
					Runtime.getRuntime().exec(new String[]{filemanager, dir.getCanonicalPath()});
				}
			} catch (IOException e) {
				env.getMc().err("Could not start file manager: " + filemanager +
						"\n" + e.getMessage());
				e.printStackTrace();
			}
		}
		else {
			env.getMc().err("No file manager set. Please set " + G.PROP_TOOL_FILEMGR + " first.");
		}
	}
	
	private void doOpenTextEditor() {
		String dirParam = StringTools.join(tokens);
		File dir = null;
		try {
			dir = env.files.resolvePath(dirParam);
		} catch (IOException e1) {			
			mc.err("Could not resolve path: " + dirParam);
			e1.printStackTrace();
			return;
		}
		 
		if (dir.isDirectory()) {
			env.getMc().err("Can't do: \"" + dir + "\" is not a file.");
			return;
		}
		String editor = Config.get(G.CFG_MAIN, G.PROP_TOOL_EDITOR);
		if (editor != null) {	
			try {
				if (editor.equalsIgnoreCase("auto")) {
					Desktop.getDesktop().open(dir);
				}
				else {
					String cmd = editor + " " + dir.getCanonicalPath();
					Runtime.getRuntime().exec(new String[]{editor, dir.getCanonicalPath()});
				}
			} catch (IOException e) {
				env.getMc().err("Could not start text editor: " + editor +
						"\n" + e.getMessage());
				e.printStackTrace();
			}
		}
		else {
			env.getMc().err("No text editor set. Please set " + G.PROP_TOOL_EDITOR + " first.");
		}
	}

	
	private void dumpKeyBinding(String keyStr) {
		Keys key = Keys.find(keyStr);
		String action = env.keys.getAction(key);
		if (key == null) {
			mc.err(keyStr + ": There is no such key.");
		}
		else if (action == null) {
			mc.err(key.getName() + " is not bound.");
		}
		else {
			mc.format("§e%s§r: %s", key.getName(), action);
		}
	}

	private void assertArgCount(int size, String errorMsg) {
		if (tokens.size() < size) {
			throw new BadUserInput(errorMsg);
		}
	}
}
