package xde.lincore.mcscript.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import xde.lincore.mcscript.Directions;
import xde.lincore.mcscript.Vector3d;

public class DirectionsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		System.out.println(Directions.getClosest(new Vector3d(567.2d, 700d, 594.678d), false));
	}

}
