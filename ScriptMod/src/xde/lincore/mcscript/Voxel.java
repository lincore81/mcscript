package xde.lincore.mcscript;

import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3;
import net.minecraft.src.Vec3Pool;

public final class Voxel implements Comparable<Voxel>{
	
	public int x, y, z;
	
	public static final Voxel ZERO 			= new Voxel( 0,  0,  0);
	public static final Voxel UP 			= new Voxel( 0,  1,  0);
	public static final Voxel DOWN 			= new Voxel( 0, -1,  0);
	public static final Voxel WEST 			= new Voxel(-1,  0,  0);
	public static final Voxel EAST 			= new Voxel( 1,  0,  0);
	public static final Voxel SOUTH 		= new Voxel( 0,  0,  1);
	public static final Voxel NORTH 		= new Voxel( 0,  0, -1);
	
	
	public Voxel(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Voxel(Voxel other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	
	public Voxel(Vector3d vec) {
		this.x = MathHelper.floor_double(vec.x);
		this.y = MathHelper.floor_double(vec.y);
		this.z = MathHelper.floor_double(vec.z);
	}
	
	
	public Voxel() {}
	
	public Voxel clone() {
		return new Voxel(x, y, z);
	}

	public Voxel add(Voxel other) {
		return new Voxel(
				this.x + other.x, 
				this.y + other.y, 
				this.z + other.z);
	}
	
	public Voxel add(int x, int y, int z) {
		return new Voxel(
				this.x + x,
				this.y + y,
				this.z + z);
	}
	
	
	public Voxel sub(Voxel other) {
		return new Voxel(
				this.x - other.x, 
				this.y - other.y, 
				this.z - other.z);
	}
	
	
	public Voxel sub(int x, int y, int z) {
		return new Voxel(
				this.x - x,
				this.y - y,
				this.z - z);
	}
	
	
	public Voxel invert() {
		return new Voxel(x * -1, y * -1, z * -1);
	}
	
	
	public Voxel abs() {
		return new Voxel(
				Math.abs(x), 
				Math.abs(y), 
				Math.abs(z));
	}
	
	public Voxel multiply(int scalar) {
		return new Voxel(
				this.x * scalar,
				this.y * scalar,
				this.z * scalar);
	}

    
    public double length() {
    	return Math.sqrt(lengthSquared());
    }
    
    public int lengthSquared() {
    	return x * x + y * y + z * z;
    }
    
	
    public double distanceTo(Voxel other)
    {
    	return Math.sqrt(squareDistanceTo(other));
    }
    
    public int squareDistanceTo(Voxel other)
    {
    	Voxel diff = other.sub(this);
    	return diff.lengthSquared();
    }
    
    
    public Voxel up(int amount) {
    	return new Voxel(this.add(UP.multiply(amount)));
    }
    
    public Voxel down(int amount) {
    	return new Voxel(this.add(DOWN.multiply(amount)));
    }
    
    public Voxel north(int amount) {
    	return new Voxel(this.add(NORTH.multiply(amount)));
    }
    
    public Voxel south(int amount) {
    	return new Voxel(this.add(SOUTH.multiply(amount)));
    }
    
    public Voxel east(int amount) {
    	return new Voxel(this.add(EAST.multiply(amount)));
    }
    
    public Voxel west(int amount) {
    	return new Voxel(this.add(WEST.multiply(amount)));
    }
    
    
//    public boolean isValid() {
//    	return true;
//    }
	
	@Override
	public String toString() {
		return String.format("(%d, %d, %d)", x, y, z);
	}
	
	protected Vec3 toVec3() {
		return Vec3.createVectorHelper(this.x, this.y, this.z);
	}
	
	protected static Voxel fromVec3(Vec3 v) {
		return new Voxel(
				MathHelper.floor_double(v.xCoord), 
				MathHelper.floor_double(v.yCoord), 
				MathHelper.floor_double(v.zCoord));
	}

	
	@Override
	public int compareTo(Voxel other) {		
		return (int)(this.length() - other.length());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Voxel)) {			
			return false;
		}		
		return equalsVoxel((Voxel)obj);
	}
	
	public boolean equalsVoxel(Voxel other) {
		return !(this.x != other.x || this.y != other.y || this.z != other.z);
	}

	public Vector3d toVector() {
		return new Vector3d(this.x, this.y, this.z);
	}
}
