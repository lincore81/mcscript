package xde.lincore.test.mcscript.edit.turtle;

import java.util.Set;

import xde.lincore.mcscript.edit.Turtle;
import xde.lincore.mcscript.edit.turtlespeak.CommonTurtleSpeak;
import xde.lincore.mcscript.edit.turtlespeak.TsArgumentTypes;
import xde.lincore.mcscript.edit.turtlespeak.TsBlock;
import xde.lincore.mcscript.edit.turtlespeak.TsKeyword;
import xde.lincore.mcscript.edit.turtlespeak.TsStatement;

public class TurtleTestDialect extends CommonTurtleSpeak {
	
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

	
	public TurtleTestDialect() {}
	

	@Override
	public TsBlock onStatement(TsStatement statement) {
		switch (statement.keyword.id) {
			case LT:
				System.out.println("Turning left");
				break;
			case RT:
				System.out.println("Turning right");
				break;
			case UP:
				System.out.println("Looking up");
				break;
			case DN:
				System.out.println("Looking down");
				break;
			case FD:
				System.out.format("Going %d blocks forward\n", statement.arguments.get(0).intValue());
				break;
			case BK:
				System.out.format("Going %d blocks backward\n", statement.arguments.get(0).intValue());
				break;
			case GOUP:
				System.out.format("Going %d blocks up\n", statement.arguments.get(0).intValue());
				break;
			case GODN:
				System.out.format("Going %d blocks down\n", statement.arguments.get(0).intValue());
				break;
			case PU:
				System.out.println("Pen up");
				break;
			case PD:
				System.out.println("Pen down");
				break;
			case RSET:
				System.out.println("Reset");
				break;
		}
		return super.onStatement(statement);
	}


	@Override
	public void registerKeywords(Set<TsKeyword> keywords) {		
		super.registerKeywords(keywords); // <-- repeat etc.

		//                         ID:       NAME:		REGEX:       	BLOCK EXP.:		ARGUMENTS:
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
}
