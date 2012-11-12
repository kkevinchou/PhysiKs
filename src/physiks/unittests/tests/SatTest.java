package physiks.unittests.tests;

import physiks.PhysSimHelper;
import physiks.collision.SeparatingAxisTest;
import physiks.entities.RigidBody;
import physiks.geometry.Vector2D;

public final class SatTest extends Test {
	@Override
	public final void setup() {
		System.out.println("=== SatTest ===");
	}

	@Override
	public final void run() {
		zeroSeparationHorizontal();
		zeroSeparationVertical();
		testSmallEpsilonOverlap();
	}
	
	private void zeroSeparationHorizontal() {
		RigidBody body1 = PhysSimHelper.createBox(0, 0, 10, 10, 1);
		RigidBody body2 = PhysSimHelper.createBox(10, 0, 10, 10, 1);
		
		Vector2D result = SeparatingAxisTest.getSeparatingAxis(body1, body2);
		
		// PolyBodies are right next to each other, technically not colliding.
		tAssert(result, new Vector2D(0, 1));
	}
	
	private void zeroSeparationVertical() {
		RigidBody body1 = PhysSimHelper.createBox(0, 0, 2, 2, 1);
		RigidBody body2 = PhysSimHelper.createBox(0, 2, 2, 2, 1);
		
		Vector2D result = SeparatingAxisTest.getSeparatingAxis(body1, body2);
		
		// PolyBodies are right next to each other, technically not colliding.
		tAssert(result, new Vector2D(1, 0));
	}
	
	private void testSmallEpsilonOverlap() {
		RigidBody body1 = PhysSimHelper.createBox(0, 0.024f, 2, 2, 1);
		RigidBody body2 = PhysSimHelper.createBox(0, 2, 2, 2, 1);
		
		Vector2D result = SeparatingAxisTest.getSeparatingAxis(body1, body2);
		tAssert(result, null);
	}
}
