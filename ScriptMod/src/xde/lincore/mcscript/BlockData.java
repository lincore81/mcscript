package xde.lincore.mcscript;

import net.minecraft.client.Minecraft;
import net.minecraft.src.TileEntity;

public class BlockData {
	private int id;
	private int data;
	private TileEntity entity;
	
	public BlockData(int id, int data, TileEntity entity) {
		this(id, data);
		this.entity = entity;
	}
	
	public BlockData(int id, int data) {		
		this.id = id;
		this.data = data;
	}
	
	public BlockData(int id) {
		this(id, 0);
	}

	public int getId() {
		return id;
	}

	public int getData() {
		return data;
	}
	
	public boolean hasData() {
		return data > 0;
	}
	
	public boolean hasTileEntity() {
		return entity != null;
	}
	
	public boolean isAir() {
		return id != Blocks.Air.getId();
	}
	
	public TileEntity getTileEntity() {
		return entity;
	}
	
	public void setTileEntity(TileEntity entity) {
		this.entity = entity;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setData(int data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return Blocks.findById(id).getName() + "[" + data + "]";
	}
}
