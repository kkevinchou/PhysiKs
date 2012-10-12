package collision;

import entities.RigidBody;
import geometry.Vector2D;

public class SeparatingAxisTest {
	private static SeparatingAxisTest instance = new SeparatingAxisTest();
	
	private SeparatingAxisTest() {
	}
	
	public SeparatingAxisTest getInstance() {
		return instance;
	}
	
	/*
	 * Finds the separating axis of two rigid bodies
	 * 
	 * Params: The two bodies to test for a separating axis
	 * Return: A vector representation of the axis. Or null if there is no separating axis (the bodies are intersecting)
	 */
	public static Vector2D findSeparatingAxis(RigidBody body1, RigidBody body2) {
		// Assumes both rigid bodies are circles
		Vector2D position1 = body1.getPosition();
		Vector2D position2 = body2.getPosition();
		
		Vector2D distanceVector = position2.sub(position1);
		float distance = distanceVector.magnitude();
		
		if (distance > (body1.getRadius() + body2.getRadius())) {
			return distanceVector.perpendicular();
		}
		
		return null;
	}
}
