package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.Vector3d;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.edit.turtlespeak.ITurtleDialect;
import xde.lincore.mcscript.edit.turtlespeak.SyntaxError;
import xde.lincore.mcscript.edit.turtlespeak.TurtleSpeakParser;
import xde.lincore.mcscript.edit.turtlespeak.VectorTurtleDialect;
import xde.lincore.mcscript.env.ScriptEnvironment;
import xde.lincore.mcscript.math.RoundingMethod;

public class VectorTurtle {

	private Blocks block;
	private Vector3d initialPosition;
	private Vector3d position;
	private Voxel blockPosition;
	private int line_stiple;
	private boolean penDown;
	private Vector3d heading;
	private TurtleSpeakParser parser;
	private RoundingMethod roundingMethod;


	public VectorTurtle(final Vector3d position) {
		this(position, Blocks.Stone, 0);
	}

	public VectorTurtle(final Vector3d position, final Blocks block, final int angle) {
		this.block = block;
		setPosition(position);
		penDown();
		look(angle);
		roundingMethod = RoundingMethod.Floor;
	}

	public VectorTurtle backward(final double distance) {
		draw(distance, false);
		return this;
	}


	public VectorTurtle forward(final double distance) {
		draw(distance, true);
		return this;
	}

	public Blocks getBlock() {
		return block;
	}

	public Vector3d getHeading() {
		return heading;
	}

	public Vector3d getPosition() {
		return position;
	}

	public double getX() {
		return position.x;
	}

	public double getY() {
		return position.y;
	}

	public double getZ() {
		return position.z;
	}

	public boolean isPenDown() {
		return penDown;
	}

	public VectorTurtle left(final int degrees) {
		heading = heading.rotateXZ(degrees);
		return this;
	}

	public VectorTurtle look(final int degrees) {
		heading = Vector3d.fromAngleXZ(degrees);
		return this;
	}

	public VectorTurtle lower(final double amount) {
		final Vector3d temp = heading;
		heading = Vector3d.DOWN;
		draw(amount, true);
		heading = temp;
		return this;
	}

	public VectorTurtle parse(final String program) {
		if (parser == null) {
			setupParser();
		}
		try {
			parser.parse(program);
			parser.eval();
		} catch (final SyntaxError e) {
			ScriptEnvironment.getInstance().chat.err(e.getMessage());
			e.printStackTrace();
		}
		return this;
	}

	public VectorTurtle pd() {
		return penDown();
	}

	public VectorTurtle penDown() {
		penDown = true;
		return this;
	}

	public VectorTurtle penUp() {
		penDown = false;
		return this;
	}

	public VectorTurtle pu() {
		return penUp();
	}


	public VectorTurtle raise(final double amount) {
		final Vector3d temp = heading;
		heading = Vector3d.UP;
		draw(amount, true);
		heading = temp;
		return this;
	}

	public VectorTurtle rememberPosition() {
		initialPosition = position;
		return this;
	}

	public VectorTurtle reset() {
		position = initialPosition;
		return this;
	}

	public VectorTurtle right(final int degrees) {
		heading = heading.rotateXZ(-degrees);
		return this;
	}

	public VectorTurtle setBlock(final Blocks block) {
		this.block = block;
		return this;
	}

	public VectorTurtle setHeading(final Vector3d heading) {
		this.heading = heading;
		return this;
	}

	public VectorTurtle setPosition(final Vector3d position) {
		this.position 	= position;
		initialPosition = position;
		blockPosition 	= position.toVoxel();
		return this;
	}

	public VectorTurtle setRoundingMethod(RoundingMethod roundingMethod) {
		this.roundingMethod = roundingMethod;
		return this;
	}

	public VectorTurtle snap() {
		position = blockPosition.toVector3d().add(0.5d, 0.5d, 0.5d);
		return this;
	}
	
	public VectorTurtle togglePen() {
		penDown = !penDown;
		return this;
	}

	public VectorTurtle translate(final Vector3d translation) {
		position.add(translation);
		return this;
	}

	public VectorTurtle turnAround() {
		heading = heading.invert();
		return this;
	}

	private void draw(final double distance, final boolean forward) {
		final IEditSession edit = ScriptEnvironment.getInstance().scripts.getScript().getEditSession();
		double moved = 0d;
		final double stepLength = 1d;
			while (moved <= distance) {
				if (forward) {
					position = position.add(heading.multiply(stepLength));
				}
				else {
					position = position.sub(heading.multiply(stepLength));
				}
				final Voxel newBlock = new Voxel(position, roundingMethod);
				if (penDown && !blockPosition.equals(newBlock)) {
					edit.setBlock(blockPosition, block);
					blockPosition = newBlock;
				}
				if (moved < distance && moved + stepLength > distance) {
					moved = distance;
				}
				else {
					moved += stepLength;
				}
			}
		if (!penDown) {
			blockPosition = position.toVoxel();
		}
	}
	
	private void setupParser() {
		final ITurtleDialect dialect = new VectorTurtleDialect(this);
		parser = new TurtleSpeakParser(dialect);
	}
}
