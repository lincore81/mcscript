package xde.lincore.mcscript.math;

public enum RoundingMethod {
	Round, Floor, Ceil, CastInt;
	
	public int round(final double value) {
		switch (this) {
			case Round:
				return (int) Math.round(value);
			case Floor:
				return (int) Math.floor(value);
			case Ceil:
				return (int) Math.ceil(value);
			case CastInt:
				return (int) value;
			default:
				throw new UnsupportedOperationException();
		}
	}
	
	public long roundToLong(final double value) {
		switch (this) {
			case Round:
				return Math.round(value);
			case Floor:
				return (long) Math.floor(value);
			case Ceil:
				return (long) Math.ceil(value);
			case CastInt:
				return (long) value;
			default:
				throw new UnsupportedOperationException();
		}
	}
	
	
}
