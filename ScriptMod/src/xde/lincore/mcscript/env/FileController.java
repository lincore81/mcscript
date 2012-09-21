package xde.lincore.mcscript.env;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import xde.lincore.mcscript.G;
import xde.lincore.mcscript.ui.BadUserInputException;
import xde.lincore.util.Config;
import xde.lincore.util.Text;

public class FileController extends AbstractController {
	
	public FileController(ScriptEnvironment env) {
		super(env);
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
	
	public void assertIsValidFile(File file) throws IOException {
		if (!file.isFile() || !file.exists()) {
			throw new IOException(file.getAbsolutePath() + " is not a valid file.");
		}
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
