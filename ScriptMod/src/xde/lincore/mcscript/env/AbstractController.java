package xde.lincore.mcscript.env;

abstract class AbstractController {
	protected ScriptEnvironment env;
	
	public AbstractController(ScriptEnvironment env) {
		this.env = env;
	}
}
