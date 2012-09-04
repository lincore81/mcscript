package xde.lincore.mcscript;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3;
import net.minecraft.src.mod_Script;

public class BindingsUser extends BindingsBase {
	

	
	protected BindingsUser(ScriptingEnvironment env) {
		super(env);
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return env.getUser().getEntityName();
	}
	
	public double getX() {
		return env.getUser().posX;
	}
	
	public double getY() {
		return env.getUser().posY;
	}
	
	public double getZ() {
		return env.getUser().posZ;
	}
	
	public Vector getPosition() {
		EntityPlayer user = env.getUser();
		return new Vector(user.posX, user.posY, user.posZ);
	}
	
	public Vector getLookVector() {
		EntityPlayer user = env.getUser();		
		return Vector.fromVec3(user.getLookVec());
	}
	
	public void teleport(Vector position) {
		EntityPlayer user = env.getUser();
		user.setPositionAndUpdate(position.x, position.y, position.z);
	}
	
	public void holdBlock(Blocks block, int quantity) {
		EntityPlayer user = env.getUser();
		int hotbarSlot = user.inventory.currentItem;		
		user.inventory.setInventorySlotContents(hotbarSlot, block.getMcStack(quantity));
	}	
	
	public void noclip(boolean state) {
		env.getUser().noClip = state;
	}
	
	public boolean hasNoclip() {
		return env.getUser().noClip;
	}
	
	public Vector raytrace(double distance) {
		MovingObjectPosition hit = env.getUser().rayTrace(distance, 1f);
//		mod_Script.LOG.info(String.format("raytrace for user %s, hit: %d %d %d, type: %s", 
//				env.getUser().username, hit.blockX, hit.blockY, hit.blockZ,  hit.typeOfHit.toString()));
		return new Vector(hit.blockX, hit.blockY, hit.blockZ);
	}
	
	public Voxel getMouseOver() {		
		Minecraft minecraft = ModLoader.getMinecraftInstance();
		if (minecraft.objectMouseOver == null) return null;
		return new Voxel(
				minecraft.objectMouseOver.blockX, 
				minecraft.objectMouseOver.blockY,
				minecraft.objectMouseOver.blockZ);
	}
	
}
