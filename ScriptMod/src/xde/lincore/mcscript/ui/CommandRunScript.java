package xde.lincore.mcscript.ui;

import java.io.ObjectInputStream.GetField;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.mod_McScript;

import org.bouncycastle.util.Strings;
import org.luaj.vm2.LuaNil;

import xde.lincore.mcscript.ScriptingEnvironment;
import xde.lincore.util.StringTools;



public final class CommandRunScript extends CommandBase {

	private ScriptingEnvironment env;
	
	private static final int RE_GRP_LANG = 2;
	private static final int RE_GRP_FILE = 3;
	private static final int RE_GRP_ARGS = 4;
	private static final int RE_GRP_EXPR = 5;
	
	private static final String DELIMITER = ",";
	private static final String ASSIGNMENT = "="; 
	
	
	public CommandRunScript(ScriptingEnvironment env) {
		this.env = env;
	}
	
	@Override
	public String getCommandName() {
		return "run";
	}
	

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
//		EntityPlayer commandUser = modInstance.getScriptUser(sender.getCommandSenderName());
//		return commandUser.isClientWorld();
		return true;
	}

	@Override
	public String getCommandUsage(ICommandSender par1iCommandSender) {
		String run = getCommandName();
		String result =
				"�f\n    /" + run + " ['-'<engine>] <expression>\n" +
				"    /" + run + " ['-'<engine>] '$'<file> [':' <arguments>]\n";		
		return result;
	}

	@Override
	public List getCommandAliases() {
		return Arrays.asList(new String[]{"."});
	}

	@Override
	public void processCommand(ICommandSender sender, String[] tokens) {		
		
		String langParam = null;
		String fileParam = null;
		String argsParam = null;
		String exprParam = null;
		String engine = null;
		String input = joinString(tokens, 0);
		String regex = "\\s*(-([A-Za-z0-9_]+))?\\s*(?:\\$([^:]+)\\s*(?:\\:\\s*(.*))?|([^$].+))";
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		
		if (m.matches()) {
			langParam = m.group(RE_GRP_LANG);
			fileParam = m.group(RE_GRP_FILE);
			argsParam = m.group(RE_GRP_ARGS);
			exprParam = m.group(RE_GRP_EXPR);
			
			if (langParam != null) langParam = langParam.trim();
			if (fileParam != null) fileParam = fileParam.trim();
			
			if (exprParam != null) { // eval expression
				env.eval(exprParam, langParam, sender.getCommandSenderName());
				return;
			}
			else if (fileParam != null) { // do file			
				Map<String, String> args = null;
				if (argsParam != null && StringTools.isValidMap(argsParam, DELIMITER, ASSIGNMENT)) {
					args = StringTools.getMap(argsParam, DELIMITER, ASSIGNMENT);
					args.put("0", fileParam);
				}
				if (args == null) args = new HashMap<String, String>(0);
				env.doFile(fileParam, langParam, args, sender.getCommandSenderName());
				return;
			}

		}
		env.getMc().echo("�6Syntax error:�r\n\t" + input);
	}
}
