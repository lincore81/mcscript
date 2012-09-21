package xde.lincore.mcscript.edit.turtlespeak;

import java.util.Deque;
import java.util.Set;

public class TsTokenAnalyzer {	
	/**
	 * A fifo stack containing all tokens of the program consists of.
	 */
	private final Deque<TsToken> 	tokens;
	
	/**
	 * All known keywords the client registered.
	 */
	private final Set<TsKeyword> 	keywords;
	
	/**
	 * A block that is automatically created and that contains the complete program after 
	 * token analyzing is finished. 
	 */
	private final TsBlock 			root;
	
	/**
	 * The block the analyzer is currently in.
	 */
	private TsBlock					currentBlock;
	
	/**
	 * The statement that is currently being parsed.
	 */
	private TsStatement 			currentStatement;
	
	/**
	 * The number of currently open blocks.
	 * Block starts ('(') increase it and block ends (')') decrease it by one. 
	 * Must be 0 at the end of the program or the program is incomplete.  
	 */
	private int						openBlockCount;
		
	
	public TsTokenAnalyzer(final Deque<TsToken> tokens, final Set<TsKeyword> keywords) {
		this.tokens 	= tokens;
		this.keywords 	= keywords;
		root 			= new TsBlock(null, null);
		currentBlock 	= root;
		openBlockCount	= 0;
	}
	
	public TsBlock run() throws SyntaxError {
		TsTokenType expectedTokenType = TsTokenType.Identifier;
		while (!tokens.isEmpty()) {
			TsToken token = tokens.pollLast();
			handleToken(token);
		}
		if (openBlockCount > 0) {
			throw new SyntaxError("Syntax error: Missing %d closing %s.", openBlockCount,
					(openBlockCount == 1)? "parenthesis" : "parentheses");
		}
		return root;
	}
	
	private void handleToken(TsToken token) throws SyntaxError {
		switch (token.type) {
			case Identifier:
				handleIdentifier(token);
				break;
			case Integer:
			case Float:
				handleArgument(token);
				break;
			case BlockStart:
				handleBlockStart(token);
				break;
			case BlockEnd:
				handleBlockEnd(token);
				break;
			default:
				throw new UnsupportedOperationException("Unsupported token type: " + token.type);
				
		}
	}
	
	private void handleIdentifier(TsToken token) throws SyntaxError {
		TsKeyword keyword = findKeyword(token);
		if (keyword == null) {
			throw new SyntaxError("Syntax error on token \"" + token.string + 
					"\": Unknown keyword.");
		}
		if (currentStatement != null) {
			assertStatementComplete(currentStatement);
		}
		currentStatement = new TsStatement(keyword);
		currentBlock.add(currentStatement);
	}

	private void handleArgument(TsToken token) throws SyntaxError {
		if (currentStatement == null) {
			throw new SyntaxError("Syntax error on token %s: Identifier expected.", token.string);
		}
		
		int argCount = currentStatement.arguments.size();
		int expectedArgCount = currentStatement.keyword.expectedArgs.size();
		
		
		TsArgumentTypes expectedArgType = currentStatement.keyword.getExpectedArgumentType(argCount);
		
		// redundant argument?
		if (expectedArgType == null) {
			throw new SyntaxError("Syntax error on token \"%s\": Too many arguments.\n" +
					"Correct syntax: \"%s\"", token.string, currentStatement.keyword.getSyntaxString());
		}
		
		if (expectedArgType.matches(token)) {
			currentStatement.arguments.add(Float.valueOf(token.string));
		}
		else {
			throw new SyntaxError("Invalid argument \"%s\": %s expexted. Syntax: %s", 
					token.string, expectedArgType.name, currentStatement.keyword.getSyntaxString());
		}
	}

	private void handleBlockStart(TsToken token) throws SyntaxError {
		if (currentStatement == null) {
			throw new SyntaxError("Missing context: Blocks must be command arguments.");
		}
		currentBlock = new TsBlock(currentBlock, currentStatement);
		currentStatement.setBlock(currentBlock);
		currentStatement = null;
		openBlockCount++;
	}
	
	private void handleBlockEnd(TsToken token) throws SyntaxError {
		// redundant block end?
		if (openBlockCount == 0) {
			throw new SyntaxError("Syntax error on token %s. No matching %s, " +
					"delete this token.", token, TsTokenType.BlockStart.name);
		}
		
		// finish the last statement in the block:
		if (currentStatement != null) {
			assertStatementComplete(currentStatement);
		}
		
		// return to the parent block
		TsStatement context = currentBlock.getContext();
		TsBlock parent 		= currentBlock.getParent();
		assertStatementComplete(context);
		currentBlock = parent;
		currentStatement = null;
		openBlockCount--;
	}
	
	private void assertStatementComplete(TsStatement statement) throws SyntaxError {
		TsKeyword keyword = statement.keyword;
		int argCount = statement.arguments.size();
		int expectedArgCount = keyword.expectedArgs.size();
		
		// Not enough arguments?
		if (argCount < expectedArgCount) {
			throw new SyntaxError("Syntax error on statement \"%s\": Not enough arguments. " +
					"Syntax: %s", statement, keyword.getSyntaxString());			
		}
		
		// Too many arguments case is checked in handleArguments
		
		// Missing body:
		if (keyword.expectsBlock && !statement.hasBlock()) {
			throw new SyntaxError("Syntax error on statement \"%s\": This command expects a command " +
					"block. Syntax: %s", statement, keyword.getSyntaxString());
		}
		//Redundant body:
		else if (!keyword.expectsBlock && statement.hasBlock()) {
			throw new SyntaxError("Syntax error on statement \"%s\": This command does " +
					"not need a command block. Syntax: %s",	statement, keyword.getSyntaxString());
		}
	}
	
	private TsKeyword findKeyword(TsToken token) {
		for (TsKeyword keyword: keywords) {
			if (keyword.matches(token)) {
				return keyword;
			}
		}
		return null;
	}
}
