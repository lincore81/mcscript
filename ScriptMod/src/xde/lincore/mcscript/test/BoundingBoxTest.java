package xde.lincore.mcscript.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import xde.lincore.mcscript.BoundingBox;
import xde.lincore.mcscript.Directions;
import xde.lincore.mcscript.Voxel;

public class BoundingBoxTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testExpand() {
		Voxel pos1 = new Voxel(60, 10, 60);
		Voxel pos2 = new Voxel(1, 1, 1);
		BoundingBox b = new BoundingBox(pos1, pos2);
		System.out.println("before: " + b.toString());		
		b = b.expand(Directions.South, 30);
		System.out.println("after: " + b.toString());
	}

	@Test
	public void testResize() {
	}

}
