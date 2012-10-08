package xde.lincore.mcscript;

public enum Dimensions {
	Overworld(0),
	Nether(-1),
	End(1);
	
	private int value;
	
	private Dimensions(final int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static Dimensions fromValue(final int value) {
		for (Dimensions d: values()) {
			if (d.value == value) return d;
		}
		throw new IllegalArgumentException();
	}
}
