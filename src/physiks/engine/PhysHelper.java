package physiks.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import physiks.entities.PolyBody;
import physiks.entities.RigidBody;
import physiks.geometry.Edge;
import physiks.geometry.Vector2D;

public abstract class PhysHelper {
	
	/**
	 * @param body1
	 * @param body2
	 * @param axis
	 * @return The amount of overlap of two bodies projected along an axis
	 */
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
		
		float b1Projection = b1Max - b1Min + 1;
		float b2Projection = b2Max - b2Min + 1;
		
		float combinedProjectionMax = Math.max(b1Max, b2Max);
		float combinedProjectionMin = Math.min(b1Min, b2Min);
		float combinedProjection = combinedProjectionMax - combinedProjectionMin + 1;
		
		if (combinedProjection >= b1Projection + b2Projection) {
			return 0;
		} else {
			float overlap = (b1Projection + b2Projection) - combinedProjection;
			return overlap;
		}
	}
	
	/**
	 * @param body1
	 * @param body2
	 * @return A list of points on body1 that's closest to body2
	 */
	public static List<Vector2D> getClosestPoints(PolyBody body1, PolyBody body2) {
		List<Vector2D> testPoints = body1.getPoints();
		List<Vector2D> edgePoints = body2.getPoints();
		
		float minDistance = Float.POSITIVE_INFINITY;
		List<Vector2D> minPoints = new ArrayList<Vector2D>();
		
		for (int i = 0, size = edgePoints.size(); i < size; i++) {
			Vector2D a = edgePoints.get(i);
			Vector2D b = edgePoints.get((i + 1) % size);
			
			for (Vector2D testPoint : testPoints) {
				float testPointDistance = pointDistanceToEdge(testPoint, a, b);
				
				if (testPointDistance < minDistance) {
					minDistance = testPointDistance;
					minPoints.clear();
					minPoints.add(testPoint);
				} else if (testPointDistance == minDistance) {
					minPoints.add(testPoint);
				}
			}
		}
		
		return minPoints;
	}
	
	/**
	 * @param body
	 * @param threshold
	 * @effect Sets the velocity to zero if it is below a certain threshold
	 */
	public static void zeroOutMicroVelocities(RigidBody body, float threshold) {
		Vector2D velocity = body.getVelocity();
		
		if (Math.abs(velocity.getX()) < threshold) {
			body.setVelocity(new Vector2D(0, velocity.getY()));
		}
		
		if (Math.abs(velocity.getY()) < threshold) {
			body.setVelocity(new Vector2D(velocity.getX(), 0));
		}
	}
	
	/*
	 * Calculates the impulse magnitude of two colliding bodies
	 * 
	 */
	
	public static float calculateImpulseMagnitude(RigidBody body1, RigidBody body2, Vector2D collisionNormal) {
		// Formula: http://chrishecker.com/images/e/e7/Gdmphys3.pdf
		
		// Assumption, collision normal is always pointing in the opposite
		// direction of the velocity
		
		float b1VelocityMagnitudeAlongCollisionNormal = Math.abs(body1.getVelocity().normalizedProjection(collisionNormal));
		Vector2D b1VelocityAlongCollisionNormal = collisionNormal.mult(-1 * b1VelocityMagnitudeAlongCollisionNormal);
		
		float b2VelocityMagnitudeAlongCollisionNormal = Math.abs(body2.getVelocity().normalizedProjection(collisionNormal));
		Vector2D b2VelocityAlongCollisionNormal = collisionNormal.mult(b2VelocityMagnitudeAlongCollisionNormal);
		
		Vector2D closingVelocityAlongCollisionNormal = b1VelocityAlongCollisionNormal.sub(b2VelocityAlongCollisionNormal);
		float combinedInverseMass = (1 / body1.getMass()) + (1 / body2.getMass());
		
		float impulse = -(1 + PhysicsEngine.coefficientOfRestitution);
		impulse = impulse * closingVelocityAlongCollisionNormal.dot(collisionNormal);
		impulse = impulse / collisionNormal.dot(collisionNormal);
		impulse = impulse / (combinedInverseMass);
		
		return impulse;
	}
	
	public static float pointDistanceToEdge(Vector2D point, Vector2D edgePointA, Vector2D edgePointB) {
		Vector2D edgeVector = edgePointA.sub(edgePointB);
		
		float distFromA = edgePointA.sub(point).magnitude();
		float distFromB = edgePointB.sub(point).magnitude();
		
		float distance = Math.min(distFromA, distFromB);
		
		// if the test point is "between" the two edge points, it is closest to the edge itself,
		// not the end points of the edge
		if ((point.sub(edgePointA).dot(edgeVector) > 0 && point.sub(edgePointB).dot(edgeVector) < 0) || 
			(point.sub(edgePointA).dot(edgeVector) < 0 && point.sub(edgePointB).dot(edgeVector) > 0)) {
			
			float perpendicularDistance = Math.abs(point.sub(edgePointA).normalizedProjection(edgeVector.perpendicular()));
			distance = perpendicularDistance;
		}
		
		return distance;
	}
}
