package xde.lincore.mcscript.edit.turtlespeak;

import java.util.Set;

import xde.lincore.mcscript.edit.VectorTurtle;

public class VectorTurtleDialect extends CommonTurtleSpeak {
		private VectorTurtle turtle;
		
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
		private static final int MEM	= 0xf2; 

		
		@Override
		public void registerKeywords(Set<TsKeyword> keywords) {
			super.registerKeywords(keywords); // for REPEAT etc.
		
			//                         ID:       NAME:		REGEX:       	BLOCK EXPECTED:	 ARGUMENTS:
			keywords.add(new TsKeyword(LT, 		"left", 	"lt|left", 		false, TsArgumentTypes.Integer));
			keywords.add(new TsKeyword(RT, 		"right", 	"rt|right", 	false, TsArgumentTypes.Integer));			
			
			keywords.add(new TsKeyword(FD, 		"forward", 	"fd|forward", 	false, TsArgumentTypes.Float));
			keywords.add(new TsKeyword(BK, 		"backward", "bk|backward", 	false, TsArgumentTypes.Float));
			keywords.add(new TsKeyword(GOUP, 	"goup", 	"goup", 		false, TsArgumentTypes.Float));
			keywords.add(new TsKeyword(GODN, 	"godown", 	"godn|godown", 	false, TsArgumentTypes.Float));
			
			keywords.add(new TsKeyword(PU, 		"penup", 	"pu|penup", 	false));
			keywords.add(new TsKeyword(PD, 		"pendown", 	"pd|pendown", 	false));
			keywords.add(new TsKeyword(RSET, 	"reset", 	"reset", 		false));
			keywords.add(new TsKeyword(MEM,		"remember", "remember|rem|mem", false));
		}


		@Override
		public TsBlock onStatement(TsStatement command) {
			switch (command.keyword.id) {
				case LT:
					turtle.left(command.arguments.get(0).intValue());
					break;
				case RT:
					turtle.right(command.arguments.get(0).intValue());
					break;
				case FD:
					turtle.forward(command.arguments.get(0));
					break;
				case BK:
					turtle.backward(command.arguments.get(0));
					break;
				case GOUP:
					turtle.raise(command.arguments.get(0));
					break;
				case GODN:
					turtle.lower(command.arguments.get(0));
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
				case MEM:
					turtle.setPosition(turtle.getPosition());
					break;
			}
			return super.onStatement(command); // returns a block to execute or null
		}


		public VectorTurtleDialect(VectorTurtle turtle) {
			this.turtle = turtle;
		}
	}
