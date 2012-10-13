package collision;

import entities.RigidBody;
import geometry.Vector2D;

public abstract class SeparatingAxisTest {
	/*
	 * Finds the separating axis of two rigid bodies
	 * 
	 * Params: The two bodies to test for a separating axis
	 * Return: A vector representation of the axis. Or null if there is no separating axis (the bodies are intersecting)
	 */
	public static SatResult findSeparatingAxis(RigidBody body1, RigidBody body2) {
		// Assumes both rigid bodies are circles
		Vector2D position1 = body1.getPosition();
		Vector2D position2 = body2.getPosition();
		
		Vector2D distanceVector = position2.sub(position1);
		float distance = distanceVector.magnitude();

		SatResult result = new SatResult();
		if (distance >= (body1.getRadius() + body2.getRadius())) {
			result.setSeparatingAxis(distanceVector.perpendicular());
		} else {
			float minimumSeparatingDistance = (body1.getRadius() + body2.getRadius()) - distance;
			Vector2D minimumSeparatingVector = distanceVector.normalize().mult(-1);
			minimumSeparatingVector = minimumSeparatingVector.mult(minimumSeparatingDistance);
			result.setMinimumSeparatingVector(minimumSeparatingVector);
		}
		
		return result;
	}
}
