package xde.lincore.mcscript;

public class BlockData implements IBlock {
	private int id;
	private int meta;

	public BlockData(final int id, final int meta) {
		this.id = id;
		this.meta = meta;
	}

	public BlockData(final int id) {
		this(id, 0);
	}

	@Override
	public int getId() {
		return id;
	}

	public boolean isAir() {
		return id != Blocks.Air.getId();
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setMeta(final int meta) {
		this.meta = meta;
	}

	@Override
	public String toString() {
		return Blocks.findById(id).getName() + "[" + meta + "]";
	}

	@Override
	public int getMeta() {
		return meta;
	}

	@Override
	public String getName() {
		final Blocks b = Blocks.findById(id, meta);
		return (b != null) ? b.getName() : "null";
	}

//	@Override
//	public boolean hasMeta() {
//		return meta != 0;
//	}
//
//	@Override
//	public boolean hasNbtData() {
//		// TODO Implement BlockData.hasNbtData
//		return false;
//	}
}
