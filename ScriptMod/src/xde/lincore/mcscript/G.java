package xde.lincore.mcscript;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.src.mod_Script;

/**
 * Static class containing constants.
 * @author lincore
 *
 */
public final class G {
	public static final String MOD_NAME 	= "mcscript";
	public static final String VERSION 		= "0.03";
	public static final String BUILD_DATE 	= "2012-09-04";

	public static final Logger LOG 			= Logger.getLogger(MOD_NAME);
	public static final File MOD_DIR 		= new File(Minecraft.getMinecraftDir(), 
														"mods/" + MOD_NAME + "/");
	public static final File CFG_DIR 		= new File(MOD_DIR, "config/");
	
	
	// Property key names:
	public static final String PROP_CWD 		= "cwd";
	public static final String PROP_FILE_MGR 	= "file.manager";
	public static final String PROP_ENCODING 	= "file.encoding";
	public static final String PROP_AUTOSAVE 	= "config.autosave";
	
	public static final String KEYBIND_TOGGLE_CONSOLE = "toggle console";

	public static final String CFG_MAIN 	= "main";
	public static final String CFG_ALIAS 	= "alias";
	public static final String CFG_BINDS	= "binds";
	public static final String CFG_EXT 		= ".cfg";
	
	private G(){}
}
