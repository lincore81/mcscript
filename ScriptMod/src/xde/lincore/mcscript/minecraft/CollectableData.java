package xde.lincore.mcscript.minecraft;

import net.minecraft.src.Item;
import xde.lincore.mcscript.ICollectable;

public class CollectableData implements ICollectable {
	private int id, meta;
	
	public CollectableData(final int id, final int meta) {
		this.id = id;
		this.meta = meta;
	}
	
	public CollectableData(final ICollectable other) {
		this.id = other.getId();
		this.meta = other.getMeta();
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getMeta() {
		return meta;
	}

	@Override
	public String getName() {
		return Item.itemsList[id].getItemNameIS(MinecraftUtils.toItemStack(this, 1));
	}

	protected void setId(final int id) {
		this.id = id;
	}

	protected void setMeta(final int meta) {
		this.meta = meta;
	}
	
}
