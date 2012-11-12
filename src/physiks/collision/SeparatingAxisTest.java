package physiks.collision;

import java.util.ArrayList;
import java.util.List;

import physiks.engine.PhysHelper;
import physiks.entities.PolyBody;
import physiks.entities.RigidBody;
import physiks.geometry.Vector2D;


public abstract class SeparatingAxisTest {
	
	/**
	 * @param a
	 * @param b
	 * @return A vector representation of a separating axis. Or null if there is no separating axis
	 */
	
	public static Vector2D getSeparatingAxis(RigidBody a, RigidBody b) {
		PolyBody body1 = (PolyBody)a;
		PolyBody body2 = (PolyBody)b;
		
		List<Vector2D> normals = new ArrayList<Vector2D>();
		normals.addAll(body1.getNormals());
		normals.addAll(body2.getNormals());
		
		for (Vector2D normal : normals) {
			float overlap = PhysHelper.overlapAlongAxis(body1, body2, normal);
			if (overlap == 0) {
				return normal.perpendicular();
			}
		}

		return null;
	}
}
