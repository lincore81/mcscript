package xde.lincore.mcscript;

import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;

public class BlockData implements IStackable, IBlock{
	private int id;
	private int meta;
	private TileEntity entity;

	public BlockData(final int id, final int data, final TileEntity entity) {
		this(id, data);
		this.entity = entity;
	}

	public BlockData(final int id, final int data) {
		this.id = id;
		meta = data;
	}

	public BlockData(final int id) {
		this(id, 0);
	}

	@Override
	public int getId() {
		return id;
	}

	public boolean hasTileEntity() {
		return entity != null;
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
		final Blocks b = Blocks.findById(id);
		assert b != null;
		return b.getName();
	}

	@Override
	public boolean hasMeta() {
		return meta != 0;
	}

	@Override
	public boolean hasNbtData() {
		// TODO Implement BlockData.hasNbtData
		return false;
	}

	@Override
	public ItemStack getItemStack(final int quantity) {
		// TODO Expand BlockData.getItemStack to consider meta data
		final Blocks b = Blocks.findById(id);
		assert b != null;
		return b.getItemStack(quantity);
	}
}
