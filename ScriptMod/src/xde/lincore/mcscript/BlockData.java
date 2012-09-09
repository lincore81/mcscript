package xde.lincore.mcscript;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;

public class BlockData implements IStackable, IBlock{
	private int id;
	private int meta;
	private TileEntity entity;
	
	public BlockData(int id, int data, TileEntity entity) {
		this(id, data);
		this.entity = entity;
	}
	
	public BlockData(int id, int data) {		
		this.id = id;
		this.meta = data;
	}
	
	public BlockData(int id) {
		this(id, 0);
	}

	public int getId() {
		return id;
	}
	
	public boolean hasTileEntity() {
		return entity != null;
	}
	
	public boolean isAir() {
		return id != Blocks.Air.getId();
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMeta(int meta) {
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
		Blocks b = Blocks.findById(id);
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
	public ItemStack getItemStack(int quantity) {
		// TODO Expand BlockData.getItemStack to consider meta data
		Blocks b = Blocks.findById(id);
		assert b != null;
		return b.getItemStack(quantity);
	}
}
