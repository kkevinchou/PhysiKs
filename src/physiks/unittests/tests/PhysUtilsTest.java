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
		overlapTest();
		pointToEdgeTest();
	}
	
	private void pointToEdgeTest() {
		Vector2D p = new Vector2D(5, 5);
		Vector2D a = new Vector2D(0, 0);
		Vector2D b = new Vector2D(20, 0);
		
		float distance = PhysHelper.pointDistanceToEdge(p, a, b);
		
		tAssert(distance, 5f);
	}
	
	private void overlapTest() {
		PolyBody a = new PolyBody(0, 0, 2, 2);
		PolyBody b = new PolyBody(0, 0, 2, 2);
		
		float overlap = PhysHelper.overlapAlongAxis(a, b, new Vector2D(1, 0));
		tAssert(overlap, 2f);
		
		PolyBody c = new PolyBody(0, 0, 2, 2);
		PolyBody d = new PolyBody(1, 0, 2, 2);
		
		overlap = PhysHelper.overlapAlongAxis(c, d, new Vector2D(1, 0));
		tAssert(overlap, 1f);
		
		PolyBody e = new PolyBody(0, 0, 4, 4);
		PolyBody f = new PolyBody(2, 0, 4, 4);
		
		overlap = PhysHelper.overlapAlongAxis(e, f, new Vector2D(1, 0));
		tAssert(overlap, 2f);
		
		PolyBody g = new PolyBody(0, 0, 4, 4);
		PolyBody h = new PolyBody(4, 4, 4, 4);
		
		overlap = PhysHelper.overlapAlongAxis(g, h, new Vector2D(1, 0));
		tAssert(overlap, 0f);
		overlap = PhysHelper.overlapAlongAxis(g, h, new Vector2D(0, -1));
		tAssert(overlap, 0f);
		
		PolyBody i = new PolyBody(0, 0, 4, 4);
		PolyBody j = new PolyBody(3, 3, 4, 4);
		
		overlap = PhysHelper.overlapAlongAxis(i, j, new Vector2D(1, 0));
		tAssert(overlap, 1f);
		overlap = PhysHelper.overlapAlongAxis(i, j, new Vector2D(0, -1));
		tAssert(overlap, 1f);
		
		PolyBody k = new PolyBody(0, 0, 4, 4);
		PolyBody l = new PolyBody(4, 0, 4, 4);
		
		overlap = PhysHelper.overlapAlongAxis(k, l, new Vector2D(1, 0));
		tAssert(overlap, 0f);
		overlap = PhysHelper.overlapAlongAxis(k, l, new Vector2D(0, -1));
		tAssert(overlap, 4f);		
	}
}