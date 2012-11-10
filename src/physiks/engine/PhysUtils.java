package physiks.engine;

import java.util.List;

import physiks.entities.PolyBody;
import physiks.entities.RigidBody;
import physiks.geometry.Vector2D;

public abstract class PhysUtils {
	public static float overlapAlongAxis(RigidBody body1, RigidBody body2, Vector2D axis) {
		PolyBody b1 = (PolyBody)body1;
		PolyBody b2 = (PolyBody)body2;
		
		float b1Min = Float.POSITIVE_INFINITY;
		float b1Max = Float.NEGATIVE_INFINITY;
		float b2Min = Float.POSITIVE_INFINITY;
		float b2Max = Float.NEGATIVE_INFINITY;
		
		List<Vector2D> b1Points = b1.getPoints();
		List<Vector2D> b2Points = b2.getPoints();
		
		for (Vector2D point : b1Points) {
			float projectionAlongAxis = point.normalizedProjection(axis);
			if (projectionAlongAxis < b1Min) {
				b1Min = projectionAlongAxis;
			}
			if (projectionAlongAxis > b1Max) {
				b1Max = projectionAlongAxis;
			}
		}
		
		for (Vector2D point : b2Points) {
			float projectionAlongAxis = point.normalizedProjection(axis);
			if (projectionAlongAxis < b2Min) {
				b2Min = projectionAlongAxis;
			}
			if (projectionAlongAxis > b2Max) {
				b2Max = projectionAlongAxis;
			}
		}
		
		if ((b1Max < b2Min) || (b2Max < b1Min)) {
			return 0;
		} else {
			float smallestMax = Math.min(b1Max, b2Max);
			float biggestMin = Math.max(b1Min, b2Min);
			float overlap = smallestMax - biggestMin;
			return overlap + 1;
		}
	}
}
