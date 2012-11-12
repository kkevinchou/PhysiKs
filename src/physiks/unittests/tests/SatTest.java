package physiks.unittests.tests;


import java.util.ArrayList;
import java.util.List;

import physiks.PhysSimHelper;
import physiks.collision.SatResult;
import physiks.collision.SeparatingAxisTest;
import physiks.entities.PolyBody;
import physiks.entities.RigidBody;
import physiks.geometry.Vector2D;


public final class SatTest extends Test {
	@Override
	public final void setup() {
		System.out.println("=== SatTest ===");
	}

	@Override
	public final void run() {
//		noIntersection();
		zeroSeparation();
//		testSmallEpsilonOverlap();
//		simpleIntersection();
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
		
//		Vector2D result = SeparatingAxisTest.getSatResult(body1, body2);
//		tAssert(result.getMinimumSeparatingVector(), new Vector2D(-1, 0));
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
		
//		SatResult result = SeparatingAxisTest.getSatResult(body1, body2);
//		tAssert(result.getMinimumSeparatingVector(), null);
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
		
		Vector2D result = SeparatingAxisTest.getSeparatingAxis(body1, body2);
		
		// PolyBodies are right next to each other, technically not colliding.
		tAssert(result, new Vector2D(0, -1));
	}
	
	private void testSmallEpsilonOverlap() {
		RigidBody body1 = PhysSimHelper.createBox(0, 0.024f, 2, 2, 1);
		RigidBody body2 = PhysSimHelper.createBox(0, 2, 2, 2, 1);
		
		Vector2D result = SeparatingAxisTest.getSeparatingAxis(body1, body2);
		tAssert(result, null);
	}
}
