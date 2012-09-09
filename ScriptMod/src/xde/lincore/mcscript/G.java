package xde.lincore.mcscript;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.src.mod_McScript;

/**
 * Static class containing constants.
 * 
 * @author lincore
 * 
 */
public final class G {
	public static final String	MOD_NAME				= "mcscript";
	public static final String	MOD_VERSION				= "0.03";
	public static final String	MOD_BUILD_DATE			= "2012-09-06";

	public static final Logger	LOG						= Logger.getLogger(MOD_NAME);
	
	public static final File	DIR_MINECRAFT			= Minecraft.getMinecraftDir();
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
	public static final String	EXT_COMPILED			= ".cpd";

	// Property key names:
	public static final String 	PROP_PROMPT				= "prompt";
	public static final String	PROP_CWD				= "file.cwd";
	public static final String	PROP_PATH				= "file.path";
	public static final String	PROP_TOOL_FILEMGR		= "tools.file-manager";
	public static final String	PROP_TOOL_EDITOR		= "tools.text-editor";
	public static final String	PROP_ENCODING			= "file.encoding";
	public static final String	PROP_AUTOSAVE			= "config.autosave";

	public static final String	BIND_TOGGLE_CONSOLE		= "toggle console";

	public static final String	CFG_MAIN				= "main";
	public static final String	CFG_ALIAS				= "alias";
	public static final String	CFG_KEYS				= "keys";

	public static final String	DEFAULT_SCRIPT_ENGINE	= "rhino";

	private G() {}
}
