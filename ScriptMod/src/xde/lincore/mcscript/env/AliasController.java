package xde.lincore.mcscript.env;

import java.util.Map;
import java.util.Map.Entry;

import xde.lincore.util.Config;

public class AliasController extends AbstractController {

	public AliasController(final ScriptEnvironment env) {
		super(env);
	}

	

	public String getAlias(final String name) {
		return Config.get(G.CFG_ALIAS, name);
	}

	public Map getAliases() {
		return Config.getMap(G.CFG_ALIAS);
	}

	public Map getSortedMap() {
		return Config.getSortedMap(G.CFG_ALIAS);
	}

	public void loadAliases() {
		Config.load(G.CFG_ALIAS);
	}
	
	public void registerAliases() {
		for (final Map.Entry alias: Config.getMap(G.CFG_ALIAS).entrySet()) {
			registerAlias((String)(alias.getKey()), (String)(alias.getValue()));
		}
	}
	

	private boolean registerAlias(final String name, final String script) {
		if (env.commands.commandExists(name) && !Config.contains(G.CFG_ALIAS, name)) { // built-in command?
			env.chat.err("There already is a command with the name \"" +
					name + "\". Please pick a different one.");
			return false;
		}
		env.commands.registerAlias(name);
		
		return true;
	}

	public void reloadAliases() {
		final String tmpName = "aliasTmp";
		Config.load(Config.generateFileName(G.CFG_ALIAS), "aliasTmp");
		for (final Map.Entry alias: Config.getMap("aliasTmp").entrySet()) {
			setAlias((String)(alias.getKey()), (String)(alias.getValue()));
		}
		Config.removeMap("aliasTmp");
	}

	public boolean removeAlias(final String name) {
		if (Config.contains(G.CFG_ALIAS, name)) {
			boolean success = env.commands.removeAlias(name);
			if (success) {
				Config.remove(G.CFG_ALIAS, name);
				Config.save(G.CFG_ALIAS);
				return true;
			}
		}
		return false;
	}

	public void setAlias(final String name, final String script) {
		if (registerAlias(name, script)) {
			Config.set(G.CFG_ALIAS, name, script);
			Config.save(G.CFG_ALIAS);
		}
	}

	public void setMultipleAliases(final Map<String, String> aliases) {
		for (final Entry<String, String> alias: aliases.entrySet()) {
			setAlias(alias.getKey(), alias.getValue());
		}
	}
}
