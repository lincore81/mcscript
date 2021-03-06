package xde.lincore.mcscript.env;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import xde.lincore.mcscript.minecraft.MinecraftUtils;
import xde.lincore.util.Text;


/**
 * Static class containing constants.
 *
 * @author lincore
 *
 */
public final class G {
	public static final String	MOD_NAME				= "mcscript";
	public static final String	MOD_VERSION				= "0.041";
	public static final String	MOD_BUILD_DATE			= "2012-09-28";

	public static final Logger	LOG						= Logger.getLogger(MOD_NAME);

	public static final File	DIR_MINECRAFT			= MinecraftUtils.getMinecraftDirectory();
	public static final File	DIR_MOD					= new File(DIR_MINECRAFT,
																"mods/" + MOD_NAME + "/");
	public static final File	DIR_MOD_HOME			= DIR_MOD;
	public static final File	DIR_CONFIG				= new File(DIR_MOD, "config/");
	public static final File	DIR_SCRIPTS				= new File(DIR_MOD, "scripts/");
	public static final File	DIR_WORLD_SAVES			= new File(DIR_MINECRAFT, "saves/");
	public static final File	DIR_CACHE				= new File(DIR_MOD, "cache/");

	public static final String	PATH_ALIAS_USER_HOME	= "^~|^-$";
	public static final String	PATH_ALIAS_MOD_HOME		= "^@";
	public static final String	PATH_ALIAS_WORLD_DIR	= "^\\*";

	public static final String	EXT_CONFIG				= ".cfg";

	// Property key names:
	public static final String 	PROP_PROMPT				= "prompt";
	public static final String	PROP_CWD				= "file.cwd";
	public static final String	PROP_PATH				= "file.path";
	public static final String	PROP_TOOL_FILEMGR		= "tools.file-manager";
	public static final String	PROP_TOOL_EDITOR		= "tools.text-editor";
	public static final String	PROP_ENCODING			= "file.encoding";
	public static final String	PROP_LINE_TERMINATOR	= "file.line-terminator";
	public static final String	PROP_AUTOSAVE			= "config.autosave";
	public static final String	PROP_DEFAULT_ENGINE		= "engine.default";
	public static final String	PROP_MULTI_THREADING	= "multi-threading";

	public static final String	BIND_TOGGLE_CONSOLE		= "toggle console";

	public static final String	CFG_MAIN				= "main";
	public static final String	CFG_ALIAS				= "alias";
	public static final String	CFG_KEYS				= "keys";

	public static final String	DEFAULT_SCRIPT_ENGINE	= "rhino";

	public static final String	CMD_SCRIPT_ENV			= "env";
	public static final String 	CMD_RUN_SCRIPT			= "run";

	public static final String 	CMD_PREFIX				= "/";

	public static final boolean	DEBUG 					= true;
	
	public static final Properties defaultProperties;
	
	
	
	static {
		defaultProperties = new Properties();
		defaultProperties.setProperty(PROP_AUTOSAVE, "yes");
		defaultProperties.setProperty(PROP_ENCODING, Text.DEFAULT_CHARSET.name());
		defaultProperties.setProperty(PROP_LINE_TERMINATOR, "auto");
		defaultProperties.setProperty(PROP_TOOL_FILEMGR, "auto");
		defaultProperties.setProperty(PROP_TOOL_EDITOR, "auto");
		defaultProperties.setProperty(PROP_PATH, ".");
		defaultProperties.setProperty(PROP_DEFAULT_ENGINE, DEFAULT_SCRIPT_ENGINE);
		defaultProperties.setProperty(PROP_MULTI_THREADING, "yes");
		String cwd = null;
		try {
			cwd = DIR_SCRIPTS.getCanonicalPath();
		} catch (final IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		defaultProperties.setProperty(PROP_CWD, cwd);
	}
	
	private G() {}
}
