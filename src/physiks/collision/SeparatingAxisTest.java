package physiks.collision;

import java.util.ArrayList;
import java.util.List;

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
	public static SatResult getSatResult(RigidBody a, RigidBody b) {
		if ((a instanceof PolyBody) && (b instanceof PolyBody)) {
			PolyBody body1 = (PolyBody)a;
			PolyBody body2 = (PolyBody)b;
			
			List<Vector2D> normals = new ArrayList<Vector2D>();
			normals.addAll(body1.getNormals());
			normals.addAll(body2.getNormals());
			
			float b1Min = Float.POSITIVE_INFINITY;
			float b1Max = Float.NEGATIVE_INFINITY;
			float b2Min = Float.POSITIVE_INFINITY;
			float b2Max = Float.NEGATIVE_INFINITY;
			
			List<Vector2D> b1Points = body1.getPoints();
			List<Vector2D> b2Points = body2.getPoints();
			
			Vector2D separatingAxis = null;
			Vector2D minSeparatingVector = null;
			float minSeparatingMagnitude = Float.POSITIVE_INFINITY;
			boolean collisionDetected = true;
			
			for (Vector2D normal : normals) {
				// Project points over the normal
				for (Vector2D point : b1Points) {
					float b1PointProjectionOnNormal = point.normalizedProjection(normal);
					if (b1PointProjectionOnNormal < b1Min) {
						b1Min = b1PointProjectionOnNormal;
					}
					if (b1PointProjectionOnNormal > b1Max) {
						b1Max = b1PointProjectionOnNormal;
					}
				}
				
				for (Vector2D point : b2Points) {
					float b2PointProjectionOnNormal = point.normalizedProjection(normal);
					if (b2PointProjectionOnNormal < b2Min) {
						b2Min = b2PointProjectionOnNormal;
					}
					if (b2PointProjectionOnNormal > b2Max) {
						b2Max = b2PointProjectionOnNormal;
					}
				}
				
				if ((b1Max <= b2Min) || (b2Max <= b1Min)) {
					// Separating axis found. No collision between the two bodies
					separatingAxis = normal.perpendicular();
					collisionDetected = false;
					break;
				} else if ((b1Min >= b2Min && b1Max <= b2Min) || (b2Min >= b1Min && b2Max <= b1Min)) {
					// TODO: Handle case where one projection is completely within the other
					System.out.println("Error: [SeparatingAxisTest.getSatResult] Unimplemented intersection case detected!");
				} else {
					float smallestMax = Math.min(b1Max, b2Max);
					float biggestMin = Math.max(b1Min, b2Min);
					float separatingMagnitude = smallestMax - biggestMin;
					
					// Mathematical errors - Using an epsilon.
					if (separatingMagnitude < 0.0001) {
						separatingAxis = normal.perpendicular();
						collisionDetected = false;
						break;
					}
					
					if (separatingMagnitude < minSeparatingMagnitude) {
						minSeparatingMagnitude = separatingMagnitude;
						minSeparatingVector = normal;

						Vector2D vectorB1ToB2 = body2.getPosition().sub(body1.getPosition());
						if (normal.normalizedProjection(vectorB1ToB2) > 0) {
							minSeparatingVector = minSeparatingVector.mult(-1);
						}
						minSeparatingVector = minSeparatingVector.mult(separatingMagnitude);
					}
				}
				
				b1Min = b2Min = Float.POSITIVE_INFINITY;
				b1Max = b2Max = Float.NEGATIVE_INFINITY;
			}
			
			SatResult result;
			if (collisionDetected) {
				result = new SatResult(null, minSeparatingVector);
			} else {
				result = new SatResult(separatingAxis, null);
			}
			return result;
		} else {
			System.out.println("Error: [SeparatingAxisTest.getSatResult] Non polybody SAT test is not supported");
			return null;
		}
	}
}
