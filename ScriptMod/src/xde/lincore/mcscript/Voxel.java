package xde.lincore.mcscript;

import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3;

public final class Voxel implements Comparable<Voxel>{

	public int x, y, z;

	public static final Voxel ZERO 			= new Voxel( 0,  0,  0);
	public static final Voxel UP 			= new Voxel( 0,  1,  0);
	public static final Voxel DOWN 			= new Voxel( 0, -1,  0);
	public static final Voxel WEST 			= new Voxel(-1,  0,  0);
	public static final Voxel EAST 			= new Voxel( 1,  0,  0);
	public static final Voxel SOUTH 		= new Voxel( 0,  0,  1);
	public static final Voxel NORTH 		= new Voxel( 0,  0, -1);


	public Voxel(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Voxel(final Voxel other) {
		x = other.x;
		y = other.y;
		z = other.z;
	}

	public Voxel(final Vector3d vec) {
		x = MathHelper.floor_double(vec.x);
		y = MathHelper.floor_double(vec.y);
		z = MathHelper.floor_double(vec.z);
	}
	
	public Voxel(final Vector3d vec, final RoundingMethod roundingMethod) {
		if (vec == null || roundingMethod == null) throw new NullPointerException();
		
		Vector3d vec_ = vec.round(roundingMethod);
		x = (int)vec_.x;
		y = (int)vec_.y;
		z = (int)vec_.z;
	}

	public Voxel() {}

	@Override
	public Voxel clone() {
		return new Voxel(x, y, z);
	}

	public Voxel add(final Voxel other) {
		return new Voxel(
				x + other.x,
				y + other.y,
				z + other.z);
	}

	public Voxel add(final int x, final int y, final int z) {
		return new Voxel(
				this.x + x,
				this.y + y,
				this.z + z);
	}


	public Voxel sub(final Voxel other) {
		return new Voxel(
				x - other.x,
				y - other.y,
				z - other.z);
	}


	public Voxel sub(final int x, final int y, final int z) {
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

	public Voxel multiply(final int scalar) {
		return new Voxel(
				x * scalar,
				y * scalar,
				z * scalar);
	}


    public double length() {
    	return Math.sqrt(lengthSquared());
    }

    public int lengthSquared() {
    	return x * x + y * y + z * z;
    }


    public double distanceTo(final Voxel other)
    {
    	return Math.sqrt(squareDistanceTo(other));
    }

    public int squareDistanceTo(final Voxel other)
    {
    	final Voxel diff = other.sub(this);
    	return diff.lengthSquared();
    }


    public Voxel up(final int amount) {
    	return new Voxel(this.add(UP.multiply(amount)));
    }

    public Voxel down(final int amount) {
    	return new Voxel(this.add(DOWN.multiply(amount)));
    }

    public Voxel north(final int amount) {
    	return new Voxel(this.add(NORTH.multiply(amount)));
    }

    public Voxel south(final int amount) {
    	return new Voxel(this.add(SOUTH.multiply(amount)));
    }

    public Voxel east(final int amount) {
    	return new Voxel(this.add(EAST.multiply(amount)));
    }

    public Voxel west(final int amount) {
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
		return Vec3.createVectorHelper(x, y, z);
	}

	protected static Voxel fromVec3(final Vec3 v) {
		return new Voxel(
				MathHelper.floor_double(v.xCoord),
				MathHelper.floor_double(v.yCoord),
				MathHelper.floor_double(v.zCoord));
	}


	@Override
	public int compareTo(final Voxel other) {
		return (int)(length() - other.length());
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof Voxel)) {
			return false;
		}
		return equalsVoxel((Voxel)obj);
	}

	public boolean equalsVoxel(final Voxel other) {
		return !(x != other.x || y != other.y || z != other.z);
	}

	public Vector3d toVector3d() {
		return new Vector3d(x, y, z);
	}
}
