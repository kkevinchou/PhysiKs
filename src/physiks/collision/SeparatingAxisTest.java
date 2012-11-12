package physiks.collision;

import java.util.ArrayList;
import java.util.List;

import physiks.engine.PhysHelper;
import physiks.entities.PolyBody;
import physiks.entities.RigidBody;
import physiks.geometry.Vector2D;


public abstract class SeparatingAxisTest {
	/*
	 * Finds the separating axis of two rigid bodies
	 * 
	 * Params: The two bodies to test for a separating axis
	 * Return: A vector representation of the axis. Or null if there is no separating axis (the bodies are intersecting)
	 */
	public static Vector2D getSeparatingAxis(RigidBody a, RigidBody b) {
		PolyBody body1 = (PolyBody)a;
		PolyBody body2 = (PolyBody)b;
		
		List<Vector2D> normals = new ArrayList<Vector2D>();
		normals.addAll(body1.getNormals());
		normals.addAll(body2.getNormals());
		
		for (Vector2D normal : normals) {
			float overlap = PhysHelper.overlapAlongAxis(body1, body2, normal);
			if (overlap > 0) {
				return normal;
			}
		}

		return null;
	}
}
