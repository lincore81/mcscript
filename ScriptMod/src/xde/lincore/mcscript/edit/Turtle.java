package xde.lincore.mcscript.edit;

import xde.lincore.mcscript.Blocks;
import xde.lincore.mcscript.Directions;
import xde.lincore.mcscript.IBlock;
import xde.lincore.mcscript.Voxel;
import xde.lincore.mcscript.edit.turtlespeak.SimpleTurtleDialect;
import xde.lincore.mcscript.edit.turtlespeak.SyntaxError;
import xde.lincore.mcscript.edit.turtlespeak.TurtleSpeakParser;
import xde.lincore.mcscript.env.ScriptEnvironment;

public class Turtle implements ITurtle {

	private IBlock 					block;
	private Voxel 					initialPosition;
	private Voxel 					currentPosition;
	private boolean 				isPenDown;
	private Directions 				direction;
	private final Directions 		lastHorizDirection;

	private TurtleSpeakParser parser;
	private SimpleTurtleDialect dialect;


	public Turtle(final Voxel position, final IBlock block, final Directions direction) {
		this.block = block;
		setBlockPosition(position);
		this.direction = direction;
		lastHorizDirection = (direction.isHorizontal())? direction : Directions.North;
		penDown();
	}

	public Turtle(final Voxel position) {
		this(position, Blocks.Stone, Directions.North);
	}



	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#backward(int) EditSession@741854be
	 */
	@Override
	public Turtle backward(final int times) {
		draw(times, false);
		return this;
	}


	public Turtle down() {
		look(Directions.Down);
		return this;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#forward(int)
	 */
	@Override
	public Turtle forward(final int times) {
		draw(times, true);
		return this;
	}

	public IBlock getBlock() {
		return block;
	}

	public Directions getDirection() {
		return direction;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#getPosition()
	 */
	@Override
	public Voxel getBlockPosition() {
		return currentPosition;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#getX()
	 */
	@Override
	public int getX() {
		return currentPosition.x;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#getY()
	 */
	@Override
	public int getY() {
		return currentPosition.y;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#getZ()
	 */
	@Override
	public int getZ() {
		return currentPosition.z;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#isPenDown()
	 */
	@Override
	public boolean isPenDown() {
		return isPenDown;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#left()
	 */
	@Override
	public Turtle left(final int amount) {
		for (int i = 0; i < amount; i++) {
			look(direction.turnLeft());
		}
		return this;
	}

	public Turtle left() {
		look(direction.turnLeft());
		return this;
	}

	public Turtle look(final Directions direction) {
		if (direction == null) {
			throw new RuntimeException("Turtle: I can't look this way.");
		}
		this.direction = direction;

//		if (direction.isHorizontal()) {
//			horizDirection = direction;
//		}
//
//		if (this.direction.isHorizontal()) {
//			this.direction = direction;
//		}

		return this;
	}

	public Turtle look(final String direction) {
		final Directions dir = Directions.get(direction);
		if (dir != null) {
			look(dir);
		}
		return this;
	}


	public Turtle lookForward() {
		direction = lastHorizDirection;
		return this;
	}



	public Turtle lower() {
		return goDown(1);
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#lower(int)
	 */
	@Override
	public Turtle goDown(final int blocks) {
		final Directions tmp = direction;
		down().forward(blocks);
		look(tmp);
		return this;
	}


	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#parse(java.lang.String)
	 */
	@Override
	public void parse(final String program) {
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
	}

	private void setupParser() {
		dialect = new SimpleTurtleDialect(this);
		parser = new TurtleSpeakParser(dialect);
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#penDown()
	 */
	@Override
	public Turtle penDown() {
		isPenDown = true;
		return this;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#penUp()
	 */
	@Override
	public Turtle penUp() {
		isPenDown = false;
		return this;
	}

	public Turtle raise() {
		final Directions tmp = direction;
		up().forward(1);
		look(tmp);
		return this;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#raise(int)
	 */
	@Override
	public Turtle goUp(final int height) {
		final Directions tmp = direction;
		up().forward(height);
		look(tmp);
		return this;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#right()
	 */
	@Override
	public Turtle right(final int amount) {
		for (int i = 0; i < amount; i++) {
			look(direction.turnRight());
		}
		return this;
	}

	public Turtle right() {
		look(direction.turnRight());
		return this;
	}

	public void setBlock(final Blocks block) {
		this.block = block;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#setPosition(xde.lincore.mcscript.geom.Voxel)
	 */
	@Override
	public Turtle setBlockPosition(final Voxel position) {
		currentPosition = position;
		initialPosition = position;
		return this;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#togglePen()
	 */
	@Override
	public Turtle togglePen() {
		isPenDown = !isPenDown;
		return this;
	}

	public Turtle translate(final Voxel translation) {
		currentPosition.add(translation);
		return this;
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#turnAround()
	 */
	@Override
	public Turtle turnAround() {
		look(direction.invert());
		return this;
	}


	public Turtle up() {
		look(Directions.Up);
		return this;
	}

	private void draw(final int times, final boolean forward) {
		final IEditSession editSession = ScriptEnvironment.getInstance().scripts.getScript().getEditSession();
		if (times == 0) {
			return;
		}
		for (int i = 0; i < times; i++) {
			if (!isValidPosition(currentPosition)) {
				continue;
			}

			if (isPenDown) {
				editSession.setBlock(currentPosition, block);
			}
			if (forward) {
				currentPosition = currentPosition.add(direction.toVoxel());
			}
			else {
				currentPosition = currentPosition.sub(direction.toVoxel());
			}
		}
	}

	private boolean isValidPosition(final Voxel position) {
		final int worldHeight = ScriptEnvironment.getInstance().scripts.getScript().
				getScriptContext().getWorld().getMaxHeight();
		return !(position.x >= 30000000 || position.x <= -30000000 ||
				 position.z >= 30000000 || position.z <= -30000000 ||
				 position.y < 0 && position.y > worldHeight);
	}

	/* (non-Javadoc)
	 * @see xde.lincore.mcscript.edit.turtle.ITurtle2#reset()
	 */
	@Override
	public void reset() {
		currentPosition = initialPosition.clone();
	}
}

