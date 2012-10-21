package physiks.unittests.tests;


import java.util.ArrayList;
import java.util.List;

import physiks.collision.SatResult;
import physiks.collision.SeparatingAxisTest;
import physiks.entities.PolyBody;
import physiks.geometry.Vector2D;


public final class SatTest extends Test {
	@Override
	public final void setup() {
		System.out.println("=== SatTest ===");
	}

	@Override
	public final void run() {
		simpleIntersection();
		noIntersection();
		zeroSeparation();
	}
	
	private void simpleIntersection() {
		List<Vector2D> points1 = new ArrayList<Vector2D>();
		points1.add(new Vector2D(0, 0));
		points1.add(new Vector2D(10, 0));
		points1.add(new Vector2D(10, 10));
		points1.add(new Vector2D(0, 10));
		
		PolyBody body1 = new PolyBody(0, 0, 100, points1);
		
		List<Vector2D> points2 = new ArrayList<Vector2D>();
		points2.add(new Vector2D(0, 0));
		points2.add(new Vector2D(10, 0));
		points2.add(new Vector2D(10, 10));
		points2.add(new Vector2D(0, 10));
		
		PolyBody body2 = new PolyBody(9, 0, 100, points2);
		
		SatResult result = SeparatingAxisTest.getSatResult(body1, body2);
		tAssert(result.getMinimumSeparatingVector(), new Vector2D(-1, 0));
	}
	
	private void noIntersection() {
		List<Vector2D> points1 = new ArrayList<Vector2D>();
		points1.add(new Vector2D(0, 0));
		points1.add(new Vector2D(10, 0));
		points1.add(new Vector2D(10, 10));
		points1.add(new Vector2D(0, 10));
		
		PolyBody body1 = new PolyBody(0, 0, 100, points1);
		
		List<Vector2D> points2 = new ArrayList<Vector2D>();
		points2.add(new Vector2D(0, 0));
		points2.add(new Vector2D(10, 0));
		points2.add(new Vector2D(10, 10));
		points2.add(new Vector2D(0, 10));
		
		PolyBody body2 = new PolyBody(200, 0, 100, points2);
		
		SatResult result = SeparatingAxisTest.getSatResult(body1, body2);
		tAssert(result.getMinimumSeparatingVector(), null);
	}
	
	private void zeroSeparation() {
		List<Vector2D> points1 = new ArrayList<Vector2D>();
		points1.add(new Vector2D(0, 0));
		points1.add(new Vector2D(10, 0));
		points1.add(new Vector2D(10, 10));
		points1.add(new Vector2D(0, 10));
		
		PolyBody body1 = new PolyBody(0, 0, 100, points1);
		
		List<Vector2D> points2 = new ArrayList<Vector2D>();
		points2.add(new Vector2D(0, 0));
		points2.add(new Vector2D(10, 0));
		points2.add(new Vector2D(10, 10));
		points2.add(new Vector2D(0, 10));
		
		PolyBody body2 = new PolyBody(10, 0, 100, points2);
		
		SatResult result = SeparatingAxisTest.getSatResult(body1, body2);
		
		// PolyBodies are right next to each other, technically not colliding.
		tAssert(result.getSeparatingAxis(), new Vector2D(0, 1));
	}
}
