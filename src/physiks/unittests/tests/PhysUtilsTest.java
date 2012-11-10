package physiks.unittests.tests;

import physiks.engine.PhysHelper;
import physiks.entities.PolyBody;
import physiks.geometry.Vector2D;

public class PhysUtilsTest extends Test {
	
	@Override
	public void setup() {
		System.out.println("=== PhysUtilsTest ===");
	}

	@Override
	public void run() {
		PolyBody a = new PolyBody(0, 0, 1, 1);
		PolyBody b = new PolyBody(0, 0, 1, 1);
		
		float overlap = PhysHelper.overlapAlongAxis(a, b, new Vector2D(1, 0));
		tAssert(overlap, 1f);
		
		PolyBody c = new PolyBody(0, 0, 1, 1);
		PolyBody d = new PolyBody(1, 0, 1, 1);
		
		overlap = PhysHelper.overlapAlongAxis(c, d, new Vector2D(1, 0));
		tAssert(overlap, 0f);
		
		PolyBody e = new PolyBody(0, 0, 4, 4);
		PolyBody f = new PolyBody(2, 0, 4, 4);
		
		overlap = PhysHelper.overlapAlongAxis(e, f, new Vector2D(1, 0));
		tAssert(overlap, 2f);
	}
}