package xde.lincore.mcscript;

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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.bouncycastle.util.Strings;

import xde.lincore.util.Config;
import xde.lincore.util.StringTools;
import xde.lincore.util.Text;


import net.minecraft.client.Minecraft;
import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.ModLoader;
import net.minecraft.src.StringUtils;
import net.minecraft.src.mod_Script;

public final class CommandScriptEnv extends CommandBase {

	
	private ScriptingEnvironment env;
	private mod_Script modInst;
	
	private ICommandSender sender;
	private BindingsMinecraft mc;
	private ArrayDeque<String> tokens;
	
	public CommandScriptEnv(mod_Script modInst, ScriptingEnvironment env) {
		this.env = env;
		this.modInst = modInst;
	}
	
	@Override
	public String getCommandName() {
		return "env";
	}

	@Override
	public String getCommandUsage(ICommandSender par1iCommandSender) {
		// TODO getCommandUsage		
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
			Keyword keyword = Keyword.findMatch(token);			
			switch(keyword) {
				case Info:
					handleInfo();
					break;
				case Alias:
					handleAliases();
					break;
				case Help:
					// TODO: handle help keyword
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
				default:
					throw new BadUserInput("Invalid argument: " + token);
			}
		}
		catch (BadUserInput e) {
			mc.echo("§c" + e.getMessage());
		}
		
		mc = null;
		sender = null;
		tokens = null;
	}

	private void handleAliases() {
		assertArgCount(1, "Missing Argument");
		String token = tokens.pollFirst();
		switch (Keyword.findMatch(token)) {
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
		assertArgCount(1, "Not enough arguments.");
		String token = tokens.pollFirst();
		switch (Keyword.findMatch(token)) {
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
		assertArgCount(1, "Not enough arguments.");		
		String token = tokens.pollFirst();		
		switch (Keyword.findMatch(token)) {
			case Manager:
				doStartFileManager();
				break;
			case List:
				doFilesList();
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
		switch (Keyword.findMatch(token)) {
			case Engines:
				doInfoEnginesList();
				break;
			case Engine:
				doInfoEngineShow();
				break;
			case Threads:
				doInfoThreadsList();
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
		String propValue = Config.get(Config.CFG_MAIN, propName);
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
			cfg = Config.CFG_MAIN;
			mc.echo("§oMod Config:");
		}
		else {
			cfg = tokens.pop();
			if (Config.getMap(cfg) == null) {
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
		boolean success = Config.load(Config.CFG_MAIN);
		mc.echo(success? "Ok." : "Properties were NOT reloaded.");
		
	}
	

	private void doConfigRemove() {
		assertArgCount(1, "Please enter the name of the property you want to remove.");

		String propName = StringTools.join(tokens, " ");			
		if (Config.remove(Config.CFG_MAIN, propName)) {
			mc.echo("Ok.");
			if (!Config.autosave(Config.CFG_MAIN)) mc.echo("§6Autosave failed!");
		}
		else {
			mc.echo("There is no property by the name of \"" + propName + "\".\n" +
					"Nothing has been removed.");
		}		
	}

	
	private void doConfigReset() {
		Config.clearMap(Config.CFG_MAIN, modInst.getDefaultProperties());	
		if (!Config.autosave(Config.CFG_MAIN)) mc.echo("§c§oAutosave failed!");		
	}
	

	private void doConfigSave() {
		boolean success = Config.save(Config.CFG_MAIN);
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
			Config.setMultiple(Config.CFG_MAIN, newProps);			
			mc.echo("Ok.");
			if (!Config.autosave(Config.CFG_MAIN)) mc.echo("§c§oAutosave failed!");
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
		File file = new File(Config.get(Config.CFG_MAIN, Globals.PROP_CWD), filename);
		String charset = Config.get(Config.CFG_MAIN, Globals.PROP_ENCODING);
		Text contents = new Text();
		try {
			contents.readFile(file, charset);
		} catch (FileNotFoundException e) {
			throw new BadUserInput("File not found: " + file.toString());
		} catch (IOException e) {
			e.printStackTrace();
			throw new BadUserInput("Could not read the file " + file.toString(), e);
		} catch (IllegalArgumentException e) {
			Config.remove(Config.CFG_MAIN, Globals.PROP_ENCODING);
			throw new BadUserInput("Invalid or unsupported file encoding: \"" + 
					charset + "\". " + Globals.PROP_ENCODING + 
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
		File dir;
		if (!tokens.isEmpty()) {
			File subdir = new File(StringTools.join(tokens));
			if (subdir.isAbsolute()) {
				dir = subdir;
			}
			else {
				dir = new File(Config.get(Config.CFG_MAIN, Globals.PROP_CWD), subdir.getPath());
			}				
		}
		else {
			dir = new File(Config.get(Config.CFG_MAIN, Globals.PROP_CWD));
		}
		
		try {
			mc.echo("§o" + dir.getCanonicalPath() + ":");			
		} catch (IOException e) {
			e.printStackTrace();
			mod_Script.LOG.warning("Could not get canonical path for " + dir.getAbsolutePath());
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

	/** Show more detailed information about an available script engine.
	 */
	private void doInfoEngineShow() {
		assertArgCount(1, "Missing argument.");

		ScriptEngineManager mgr = env.getManager();
		String engineName = tokens.pollFirst();
		ScriptEngine engine;
		if (Keyword.Default.matches(engineName)) {			
			mc.echo("§oThe default script engine is:");
			engine = env.getDefaultEngine();
		}
		else if (Keyword.Current.matches(engineName)) {			
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
			
			doInfoShowEngine(fac);
			mc.echo("  Mimes: " + mimes);
			mc.echo("  Compilable? " + compilable);
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
		File file = new File(mod_Script.MOD_DIR, filename);
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
		ScriptEngine engine = env.getManager().getEngineByExtension(env.getFileExtension(file.toString()));
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
		String subdir = null;
		
		if (!tokens.isEmpty()) {
			subdir = tokens.pollFirst();
		}
		else {
			subdir = "";
		}
		File dir = new File(mod_Script.MOD_DIR, subdir);
		String filemanager = Config.get(Config.CFG_MAIN, Globals.PROP_FILE_MANAGER);			
		if (filemanager != null) {				
			try {					
				String cmd = filemanager + " " + dir.getCanonicalPath();
				System.out.println("Starting file manager: \"" + cmd + "\"");
				Runtime.getRuntime().exec(new String[]{filemanager, dir.getCanonicalPath()});
			} catch (IOException e) {
				env.getMc().echo("§6Could not start file manager: " + filemanager +
						"\n" + e.getMessage());
				e.printStackTrace();
			}
		}
		else {
			env.getMc().echo("§6No file manager set. Please set \"tools.filemanager\" first.");
		}
	}

	
	private void assertArgCount(int size, String errorMsg) {
		if (tokens.size() < size) {
			throw new BadUserInput(errorMsg);
		}
	}
}
