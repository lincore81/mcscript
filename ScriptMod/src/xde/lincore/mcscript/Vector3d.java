package xde.lincore.mcscript;

import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3;
import net.minecraft.src.Vec3Pool;

public final class Vector3d implements Comparable<Vector3d>{
	
	public double x, y, z;
	
	public static final Vector3d ZERO 			= new Vector3d( 0d,  0d,  0d);
	public static final Vector3d UP 			= new Vector3d( 0d,  1d,  0d);
	public static final Vector3d DOWN 			= new Vector3d( 0d, -1d,  0d);
	public static final Vector3d WEST 			= new Vector3d(-1d,  0d,  0d);
	public static final Vector3d EAST 			= new Vector3d( 1d,  0d,  0d);
	public static final Vector3d SOUTH 			= new Vector3d( 0d,  0d,  1d);
	public static final Vector3d NORTH 			= new Vector3d( 0d,  0d, -1d);
	
	
	public static final double PI2 = Math.PI * 2;
	public static final double RAD_TO_DEG = 360d / PI2;
	
	
	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3d(Vector3d other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	
	public Vector3d(Voxel other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	
	
	public Vector3d add(Vector3d other) {
		return new Vector3d(
				this.x + other.x, 
				this.y + other.y, 
				this.z + other.z);
	}
	
	public Vector3d add(double x, double y, double z) {
		return new Vector3d(
				this.x + x,
				this.y + y,
				this.z + z);
	}
	
	
	public Vector3d sub(Vector3d other) {
		return new Vector3d(
				this.x - other.x, 
				this.y - other.y, 
				this.z - other.z);
	}
	
	
	public Vector3d sub(double x, double y, double z) {
		return new Vector3d(
				this.x - x,
				this.y - y,
				this.z - z);
	}
	
	public Vector3d floor() {
		return new Vector3d(
				MathHelper.floor_double(x),
				MathHelper.floor_double(y),
				MathHelper.floor_double(z));
	}
	
	
	public Vector3d invert() {
		return new Vector3d(x * -1d, y * -1d, z * -1d);
	}
	
	
	public Vector3d abs() {
		return new Vector3d(
				Math.abs(x), 
				Math.abs(y), 
				Math.abs(z));
	}
	
	public Vector3d multiply(double scalar) {
		return new Vector3d(
				this.x * scalar,
				this.y * scalar,
				this.z * scalar);
	}
	
	
	public Vector3d crossProduct(Vector3d other) {
		return new Vector3d(
				this.y * other.z - this.z * other.y, 
				this.z * other.x - this.x * other.z, 
				this.x * other.y - this.y * other.x);
	}
	
	
    public double dotProduct(Vector3d other) {
        return 	this.x * other.x + 
        		this.y * other.y + 
        		this.z * other.z;
    }

    
    public Vector3d normalise() {
    	double len = length();
    	return (len < 0.0001d)? ZERO : new Vector3d(x / len, y / len, z / len);
    }
    
    public double length() {
    	return Math.sqrt(lengthSquared());
    }
    
    public double lengthSquared() {
    	return x * x + y * y + z * z;
    }
    
	
    public double distanceTo(Vector3d other)
    {
    	return Math.sqrt(squareDistanceTo(other));
    }
    
    public double squareDistanceTo(Vector3d other)
    {
    	Vector3d diff = other.sub(this);
    	return diff.lengthSquared();
    }
    
    /**
     * Calculate the angle of this vector in radians.
     * Only use this method on normalised vectors for correct results.
     * @return The angle in radians from 0 - 2PI (the result differs from Math.atan2!)
     */
    public double getThetaXZ() {   	
    	double theta = Math.atan2(z, x);
    	return (theta < 0d)? theta + PI2 : theta; // convert from -PI..PI to 0..2PI range
    }
    
    /**
     * Calculate the angle of this vector in degrees.
     * Only use this method on normalised vectors for correct results.
     * @return The angle in degrees from 0� - 360�
     */
    public double getAngleXZ() {
    	return getThetaXZ() * RAD_TO_DEG;
    }
    
    public Vector3d setAngleXZ(double angle) {
    	double len = length();    	
    	return fromAngleXZ(angle).multiply(len);
    }
    
    public static Vector3d fromAngleXZ(double angle) {
    	double x = Math.cos(angle / RAD_TO_DEG);
    	double z = Math.sin(angle / RAD_TO_DEG);    	
    	return new Vector3d(x, 0d, z);
    }
    
    public Vector3d rotateXZ(double angle) {
    	return fromAngleXZ(getAngleXZ() + angle);
    }
    
    
    public Vector3d up(double amount) {
    	return new Vector3d(this.add(UP.multiply(amount)));
    }
    
    public Vector3d down(double amount) {
    	return new Vector3d(this.add(DOWN.multiply(amount)));
    }
    
    public Vector3d north(double amount) {
    	return new Vector3d(this.add(NORTH.multiply(amount)));
    }
    
    public Vector3d south(double amount) {
    	return new Vector3d(this.add(SOUTH.multiply(amount)));
    }
    
    public Vector3d east(double amount) {
    	return new Vector3d(this.add(EAST.multiply(amount)));
    }
    
    public Vector3d west(double amount) {
    	return new Vector3d(this.add(WEST.multiply(amount)));
    }
    
    public boolean isValid() {
    	return !(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z));
    }
	
	@Override
	public String toString() {
		return String.format("(%.2f, %.2f, %.2f)", x, y, z);
	}
	
	public Vec3 toVec3() {
		return Vec3.createVectorHelper(this.x, this.y, this.z);
	}
	
	public static Vector3d fromVec3(Vec3 v) {
		return new Vector3d(v.xCoord, v.yCoord, v.zCoord);
	}
	
	public Voxel toVoxel() {
		return new Voxel(this);
	}
	
	@Override
	public int compareTo(Vector3d other) {		
		return (int)(this.length() - other.length());
	}
}
