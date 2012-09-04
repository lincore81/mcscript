package xde.lincore.mcscript;

import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3;
import net.minecraft.src.Vec3Pool;

public final class Vector implements Comparable<Vector>{
	
	public double x, y, z;
	
	public static final Vector ZERO 			= new Vector( 0d,  0d,  0d);
	public static final Vector UP 				= new Vector( 0d,  1d,  0d);
	public static final Vector DOWN 			= new Vector( 0d, -1d,  0d);
	public static final Vector WEST 			= new Vector(-1d,  0d,  0d);
	public static final Vector EAST 			= new Vector( 1d,  0d,  0d);
	public static final Vector SOUTH 			= new Vector( 0d,  0d,  1d);
	public static final Vector NORTH 			= new Vector( 0d,  0d, -1d);
	
	
	public static final double PI2 = Math.PI * 2;
	public static final double RAD_TO_DEG = 360d / PI2;
	
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector(Vector other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	
	public Vector(Voxel other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	
	
	public Vector add(Vector other) {
		return new Vector(
				this.x + other.x, 
				this.y + other.y, 
				this.z + other.z);
	}
	
	public Vector add(double x, double y, double z) {
		return new Vector(
				this.x + x,
				this.y + y,
				this.z + z);
	}
	
	
	public Vector sub(Vector other) {
		return new Vector(
				this.x - other.x, 
				this.y - other.y, 
				this.z - other.z);
	}
	
	
	public Vector sub(double x, double y, double z) {
		return new Vector(
				this.x - x,
				this.y - y,
				this.z - z);
	}
	
	public Vector floor() {
		return new Vector(
				MathHelper.floor_double(x),
				MathHelper.floor_double(y),
				MathHelper.floor_double(z));
	}
	
	
	public Vector invert() {
		return new Vector(x * -1d, y * -1d, z * -1d);
	}
	
	
	public Vector abs() {
		return new Vector(
				Math.abs(x), 
				Math.abs(y), 
				Math.abs(z));
	}
	
	public Vector multiply(double scalar) {
		return new Vector(
				this.x * scalar,
				this.y * scalar,
				this.z * scalar);
	}
	
	
	public Vector crossProduct(Vector other) {
		return new Vector(
				this.y * other.z - this.z * other.y, 
				this.z * other.x - this.x * other.z, 
				this.x * other.y - this.y * other.x);
	}
	
	
    public double dotProduct(Vector other) {
        return 	this.x * other.x + 
        		this.y * other.y + 
        		this.z * other.z;
    }

    
    public Vector normalise() {
    	double len = length();
    	return (len < 0.0001d)? ZERO : new Vector(x / len, y / len, z / len);
    }
    
    public double length() {
    	return Math.sqrt(lengthSquared());
    }
    
    public double lengthSquared() {
    	return x * x + y * y + z * z;
    }
    
	
    public double distanceTo(Vector other)
    {
    	return Math.sqrt(squareDistanceTo(other));
    }
    
    public double squareDistanceTo(Vector other)
    {
    	Vector diff = other.sub(this);
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
     * @return The angle in degrees from 0° - 360°
     */
    public double getAngleXZ() {
    	return getThetaXZ() * RAD_TO_DEG;
    }
    
    public Vector setAngleXZ(double angle) {
    	double len = length();    	
    	return fromAngleXZ(angle).multiply(len);
    }
    
    public static Vector fromAngleXZ(double angle) {
    	double x = Math.cos(angle / RAD_TO_DEG);
    	double z = Math.sin(angle / RAD_TO_DEG);    	
    	return new Vector(x, 0d, z);
    }
    
    public Vector rotateXZ(double angle) {
    	return fromAngleXZ(getAngleXZ() + angle);
    }
    
    
    public Vector up(double amount) {
    	return new Vector(this.add(UP.multiply(amount)));
    }
    
    public Vector down(double amount) {
    	return new Vector(this.add(DOWN.multiply(amount)));
    }
    
    public Vector north(double amount) {
    	return new Vector(this.add(NORTH.multiply(amount)));
    }
    
    public Vector south(double amount) {
    	return new Vector(this.add(SOUTH.multiply(amount)));
    }
    
    public Vector east(double amount) {
    	return new Vector(this.add(EAST.multiply(amount)));
    }
    
    public Vector west(double amount) {
    	return new Vector(this.add(WEST.multiply(amount)));
    }
    
    public boolean isValid() {
    	return !(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z));
    }
	
	@Override
	public String toString() {
		return String.format("(%.2f, %.2f, %.2f)", x, y, z);
	}
	
	protected Vec3 toVec3() {
		return Vec3.createVectorHelper(this.x, this.y, this.z);
	}
	
	protected static Vector fromVec3(Vec3 v) {
		return new Vector(v.xCoord, v.yCoord, v.zCoord);
	}
	
	public Voxel toVoxel() {
		return new Voxel(this);
	}
	
	@Override
	public int compareTo(Vector other) {		
		return (int)(this.length() - other.length());
	}
}
