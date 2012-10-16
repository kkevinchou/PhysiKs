package collision;

import entities.RigidBody;
import geometry.Vector2D;

public abstract class CollisionChecker {
	public static CollisionResult check(RigidBody body1, RigidBody body2) {
		CollisionResult result = new CollisionResult();
		
		SatResult satResult = SeparatingAxisTest.getSatResult(body1, body2);
		Vector2D minimumSeparatingVector = satResult.getMinimumSeparatingVector();
		result.setMinimumSeparatingVector(minimumSeparatingVector);
		
		if (minimumSeparatingVector != null) {
			result.setCollisionNormal(minimumSeparatingVector.normalize());
		}
		
		return result;
	}
}
