package xde.lincore.mcscript.env;

import java.io.File;
import java.io.IOException;

import xde.lincore.util.Config;

public class FileController extends AbstractController {

	public FileController(final ScriptEnvironment env) {
		super(env);
	}

	public File getCurrentWorldDir() {
		return new File(G.DIR_WORLD_SAVES, getCurrentWorldDirName());
	}

	public String getCurrentWorldDirName() {
		return env.getScriptContext().server.getWorldDirectoryString();
		//return env.getServer().theWorldServer[0].getSaveHandler().getSaveDirectoryName();
		//return getUser().worldObj.getSaveHandler().getSaveDirectoryName();
	}

	public File getCwd() {
		final String cwd = Config.get(G.CFG_MAIN, G.PROP_CWD);
		final File cwdPath = new File(cwd);
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

	public String getFileExtension(final String fileName) {
		if (fileName.length() == 0 || fileName.endsWith("."))
		 {
			return null; // there's no extension to get
		}

		for (int i = fileName.length() - 1; i >= 0; i--) { // look for a '.' and return everything after it.
			if (fileName.charAt(i) == '.') {
				return fileName.substring(i + 1);
			}
		}
		return null; // no extension found
	}

	public void assertIsValidFile(final File file) throws IOException {
		if (!file.isFile() || !file.exists()) {
			throw new IOException(file.getAbsolutePath() + " is not a valid file.");
		}
	}

	public File resolvePath(final String path) throws IOException {
		if (path == null || path.isEmpty()) {
			return getCwd();
		}

		final String resolvedPath = path
				.replaceFirst(G.PATH_ALIAS_MOD_HOME, G.DIR_MOD_HOME.getAbsolutePath().
						replace("\\", "\\\\"))
				.replaceFirst(G.PATH_ALIAS_WORLD_DIR, getCurrentWorldDir().getAbsolutePath().
						replace("\\", "\\\\"))
				.replaceFirst(G.PATH_ALIAS_USER_HOME, System.getProperty("user.home").
						replace("\\", "\\\\"));

		if (!resolvedPath.isEmpty()) {
			final File subdirPath = new File(resolvedPath);
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

	public boolean setCwd(final File cwd) {
		if (!cwd.exists() || !cwd.isDirectory()) {
			return false;
		}
		String cwdStr;
		try {
			cwdStr = cwd.getCanonicalPath();
		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		}
		Config.set(G.CFG_MAIN, G.PROP_CWD, cwdStr);
		return true;
	}
}
