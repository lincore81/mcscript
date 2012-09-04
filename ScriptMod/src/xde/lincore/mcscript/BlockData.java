package xde.lincore.mcscript;

import net.minecraft.client.Minecraft;

public class BlockData {
	private Voxel position;
	private int id;
	private int metadata;
	
	
	public BlockData(Voxel position, int id, int metadata) {
		this.position = position;
		this.id = id;
		this.metadata = metadata;
	}
	
	public BlockData(Voxel position, int id) {
		this(position, id, 0);
	}
	
	
	public Voxel getPosition() {
		return position;
	}

	public int getId() {
		return id;
	}

	public int getMetadata() {
		return metadata;
	}

	public void setPosition(Voxel position) {
		this.position = position;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMetadata(int metadata) {
		this.metadata = metadata;
	}

	

}
