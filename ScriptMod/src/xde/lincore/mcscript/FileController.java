package xde.lincore.mcscript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import xde.lincore.mcscript.exception.BadUserInput;
import xde.lincore.util.Config;
import xde.lincore.util.Text;

public class FileController {
	
	private final ScriptingEnvironment env;
	
	public FileController(ScriptingEnvironment env) {
		this.env = env;
	}

	public File getCurrentWorldDir() {
		return new File(G.DIR_WORLD_SAVES, getCurrentWorldDirName());
	}
	
	public String getCurrentWorldDirName() {
		return env.getServer().theWorldServer[0].getSaveHandler().getSaveDirectoryName();
		//return getUser().worldObj.getSaveHandler().getSaveDirectoryName();
	}
	
	public File getCwd() {
		String cwd = Config.get(G.CFG_MAIN, G.PROP_CWD);
		File cwdPath = new File(cwd);
		if (cwdPath.isAbsolute()) {
			return cwdPath;
		}
		else {			
			return new File(G.DIR_MOD_HOME, cwd);
		}
	}
	
	public String getCwdString() {
		return Config.get(G.CFG_MAIN, G.PROP_CWD);
	}
	
	public String getFileExtension(String fileName) {
		if (fileName.length() == 0 || fileName.endsWith(".")) return null; // there's no extension to get
		
		for (int i = fileName.length() - 1; i >= 0; i--) { // look for a '.' and return everything after it.
			if (fileName.charAt(i) == '.') {
				return fileName.substring(i + 1);
			}
		}		
		return null; // no extension found
	}
	
	public String readScriptFile(File scriptfile) {
		Text result = new Text();
		String charset = Config.get(G.CFG_MAIN, G.PROP_ENCODING);
		try {
			result.readFile(scriptfile, charset);
		} catch (FileNotFoundException e) {
			env.getMc().err("File not found: " + scriptfile.toString());
			return null;
		} catch (IOException e) {
			env.getMc().err("An error occured while trying to access the file " + scriptfile.toString());
			env.getMc().echo(e.getMessage());
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			Config.remove(G.CFG_MAIN, G.PROP_ENCODING);
			throw new BadUserInput("Invalid or unsupported file encoding: \"" + 
					charset + "\". " + G.PROP_ENCODING + 
					" has been reset to " + Text.DEFAULT_CHARSET.name());
		}
		return result.toString();
	}
	
	public File resolvePath(String path) throws IOException {
		if (path == null || path.isEmpty()) return getCwd();
		
		String resolvedPath = path
				.replaceFirst(G.PATH_ALIAS_MOD_HOME, G.DIR_MOD_HOME.getAbsolutePath().
						replace("\\", "\\\\"))
				.replaceFirst(G.PATH_ALIAS_WORLD_DIR, getCurrentWorldDir().getAbsolutePath().
						replace("\\", "\\\\"))
				.replaceFirst(G.PATH_ALIAS_USER_HOME, System.getProperty("user.home").
						replace("\\", "\\\\"));
		
		if (!resolvedPath.isEmpty()) {
			File subdirPath = new File(resolvedPath);
			if (subdirPath.isAbsolute()) {
				return subdirPath;
			}
			else {
				return new File(getCwd(), resolvedPath);
			}
		}
		else {
			return getCwd();
		}
	}
	
	public boolean setCwd(File cwd) {
		if (!cwd.exists() || !cwd.isDirectory()) {
			return false;
		}
		String cwdStr;
		try {
			cwdStr = cwd.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		Config.set(G.CFG_MAIN, G.PROP_CWD, cwdStr);
		return true;
	}
}
