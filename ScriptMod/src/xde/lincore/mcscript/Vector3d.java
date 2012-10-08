package xde.lincore.mcscript;

import xde.lincore.mcscript.math.RoundingMethod;

public final class Vector3d implements Comparable<Vector3d>{

	public final double x, y, z;

	public static final Vector3d ZERO 			= new Vector3d( 0d,  0d,  0d);
	public static final Vector3d UP 			= new Vector3d( 0d,  1d,  0d);
	public static final Vector3d DOWN 			= new Vector3d( 0d, -1d,  0d);
	public static final Vector3d WEST 			= new Vector3d(-1d,  0d,  0d);
	public static final Vector3d EAST 			= new Vector3d( 1d,  0d,  0d);
	public static final Vector3d SOUTH 			= new Vector3d( 0d,  0d,  1d);
	public static final Vector3d NORTH 			= new Vector3d( 0d,  0d, -1d);
	
	public static final double PI2 = Math.PI * 2;
	public static final double RAD_TO_DEG = 360d / PI2;


	public Vector3d(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d(final Vector3d other) {
		x = other.x;
		y = other.y;
		z = other.z;
	}

	public Vector3d(final Voxel other) {
		x = other.x;
		y = other.y;
		z = other.z;
	}


	public Vector3d add(final Vector3d other) {
		return new Vector3d(
				x + other.x,
				y + other.y,
				z + other.z);
	}

	public Vector3d add(final double x, final double y, final double z) {
		return new Vector3d(
				this.x + x,
				this.y + y,
				this.z + z);
	}


	public Vector3d sub(final Vector3d other) {
		return new Vector3d(
				x - other.x,
				y - other.y,
				z - other.z);
	}


	public Vector3d sub(final double x, final double y, final double z) {
		return new Vector3d(
				this.x - x,
				this.y - y,
				this.z - z);
	}

	public Vector3d floor() {
		return new Vector3d(
				RoundingMethod.Floor.round(x),
				RoundingMethod.Floor.round(y),
				RoundingMethod.Floor.round(z));
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

	public Vector3d multiply(final double scalar) {
		return new Vector3d(
				x * scalar,
				y * scalar,
				z * scalar);
	}


	public Vector3d crossProduct(final Vector3d other) {
		return new Vector3d(
				y * other.z - z * other.y,
				z * other.x - x * other.z,
				x * other.y - y * other.x);
	}


    public double dotProduct(final Vector3d other) {
        return 	x * other.x +
        		y * other.y +
        		z * other.z;
    }


    public Vector3d normalise() {
    	final double len = length();
    	return (len < 0.0001d)? ZERO : new Vector3d(x / len, y / len, z / len);
    }

    public double length() {
    	return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
    	return x * x + y * y + z * z;
    }


    public double distanceTo(final Vector3d other)
    {
    	return Math.sqrt(squareDistanceTo(other));
    }

    public double squareDistanceTo(final Vector3d other)
    {
    	final Vector3d diff = other.sub(this);
    	return diff.lengthSquared();
    }

    /**
     * Calculate the angle of this vector in radians.
     * Only use this method on normalised vectors for correct results.
     * @return The angle in radians from 0 - 2PI (the result differs from Math.atan2!)
     */
    public double getThetaXZ() {
    	final double theta = Math.atan2(z, x);
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

    public Vector3d setAngleXZ(final double angle) {
    	final double len = length();
    	return fromAngleXZ(angle).multiply(len);
    }

    public static Vector3d fromAngleXZ(final double angle) {
    	final double x = Math.cos(angle / RAD_TO_DEG);
    	final double z = Math.sin(angle / RAD_TO_DEG);
    	return new Vector3d(x, 0d, z);
    }

    public Vector3d rotateXZ(final double angle) {
    	return fromAngleXZ(getAngleXZ() + angle);
    }


    public Vector3d up(final double amount) {
    	return new Vector3d(this.add(UP.multiply(amount)));
    }

    public Vector3d down(final double amount) {
    	return new Vector3d(this.add(DOWN.multiply(amount)));
    }

    public Vector3d north(final double amount) {
    	return new Vector3d(this.add(NORTH.multiply(amount)));
    }

    public Vector3d south(final double amount) {
    	return new Vector3d(this.add(SOUTH.multiply(amount)));
    }

    public Vector3d east(final double amount) {
    	return new Vector3d(this.add(EAST.multiply(amount)));
    }

    public Vector3d west(final double amount) {
    	return new Vector3d(this.add(WEST.multiply(amount)));
    }

    public boolean isValid() {
    	return !(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z));
    }

	@Override
	public String toString() {
		return String.format("(%.3f, %.3f, %.3f)", x, y, z);
	}

//	public Vec3 toVec3() {
//		return Vec3.createVectorHelper(x, y, z);
//	}
//
//	public static Vector3d fromVec3(final Vec3 v) {
//		return new Vector3d(v.xCoord, v.yCoord, v.zCoord);
//	}

	public Voxel toVoxel() {
		return new Voxel(this);
	}

	@Override
	public int compareTo(final Vector3d other) {
		return (int)(length() - other.length());
	}

	public Vector3d round(final RoundingMethod roundingMethod) {
		return new Vector3d(
				roundingMethod.round(x),
				roundingMethod.round(y),
				roundingMethod.round(z));
	}
}
