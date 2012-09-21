package xde.lincore.mcscript.edit.turtlespeak;

import java.util.Set;

import xde.lincore.mcscript.edit.Turtle;

public class SimpleTurtleDialect extends CommonTurtleSpeak {
	private Turtle turtle;
	
	/**
	 * These constants are only used for better readability.
	 */
	private static final int LT 	= 0x01;
	private static final int RT 	= 0x02; 
	private static final int UP		= 0x03;
	private static final int DN		= 0x04; 
		
	private static final int FD		= 0x11;
	private static final int BK		= 0x12;
	private static final int GOUP	= 0x13;
	private static final int GODN	= 0x14;
	
	private static final int PU  	= 0x21;
	private static final int PD  	= 0x22;
	
	private static final int RSET	= 0xf1;

	
	@Override
	public void registerKeywords(Set<TsKeyword> keywords) {
		super.registerKeywords(keywords); // for REPEAT etc.
	
		//                         ID:       NAME:		REGEX:       	BLOCK EXPECTED:	 ARGUMENTS:
		keywords.add(new TsKeyword(LT, 		"left", 	"lt|left", 		false));
		keywords.add(new TsKeyword(RT, 		"right", 	"rt|right", 	false));
		keywords.add(new TsKeyword(UP, 		"up", 		"up", 			false));
		keywords.add(new TsKeyword(DN, 		"down", 	"dn|down", 		false));
		
		keywords.add(new TsKeyword(FD, 		"forward", 	"fd|forward", 	false, TsArgumentTypes.Integer));
		keywords.add(new TsKeyword(BK, 		"backward", "bk|backward", 	false, TsArgumentTypes.Integer));
		keywords.add(new TsKeyword(GOUP, 	"goup", 	"goup", 		false, TsArgumentTypes.Integer));
		keywords.add(new TsKeyword(GODN, 	"godown", 	"godn|godown", 	false, TsArgumentTypes.Integer));
		
		keywords.add(new TsKeyword(PU, 		"penup", 	"pu|penup", 	false));
		keywords.add(new TsKeyword(PD, 		"pendown", 	"pd|pendown", 	false));
		keywords.add(new TsKeyword(RSET, 	"reset", 	"reset", 		false));
	}


	@Override
	public TsBlock onStatement(TsStatement command) {
		switch (command.keyword.id) {
			case LT:
				turtle.left(1);
				break;
			case RT:
				turtle.right(1);
				break;
			case UP:
				turtle.up();
				break;
			case DN:
				turtle.down();
				break;
			case FD:
				turtle.forward(command.arguments.get(0).intValue());
				break;
			case BK:
				turtle.backward(command.arguments.get(0).intValue());
				break;
			case GOUP:
				turtle.goUp(command.arguments.get(0).intValue());
				break;
			case GODN:
				turtle.goDown(command.arguments.get(0).intValue());
				break;
			case PU:
				turtle.penUp();
				break;
			case PD:
				turtle.penDown();
				break;
			case RSET:
				turtle.reset();
				break;
		}
		return super.onStatement(command); // returns a block to execute or null
	}


	public SimpleTurtleDialect(Turtle turtle) {
		this.turtle = turtle;
	}
}