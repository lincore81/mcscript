package xde.lincore.util;

public class UniqueId {
	private static int id = 0;
	
	public static int next() {
		return id++;
	}
}
