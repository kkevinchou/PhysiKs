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
		
		if ((b1Max < b2Min) || (b2Max < b1Min)) {
			return 0;
		} else {
			float smallestMax = Math.min(b1Max, b2Max);
			float biggestMin = Math.max(b1Min, b2Min);
			float overlap = smallestMax - biggestMin;
			return overlap + 1;
		}
	}
	
	/*
	 * Gets the closest points on body2 with respect to body1
	 * 
	 * Parameters: Two bodies
	 * Return: A list of (Vector2D a, List<Edges> b)
	 *     Where a is the closest point and b is the edges that it is closest to
	 */
	
	// TODO: might make more sense to have a getClosestEdges function (return closest edge as well 
	// as the points that resulted in it being the closest).  However, this function may be useful as well
	public static Vector2D getClosestPoints(PolyBody body1, PolyBody body2) {
		List<Vector2D> testPoints = body2.getPoints();
		List<Vector2D> edgePoints = body1.getPoints();
		List<Pair<Vector2D,List<Edge>>> closestPointsToEdges = new ArrayList<Pair<Vector2D,List<Edge>>>();
		
		Vector2D minPoint = null;
		float minDistance = Float.POSITIVE_INFINITY;
		
		for (int i = 0, size = edgePoints.size(); i < size; i++) {
			Vector2D a = edgePoints.get(i);
			Vector2D b = edgePoints.get((i + 1) % size);
			
			Vector2D edgeVector = b.sub(a);
			
			for (Vector2D testPoint : testPoints) {
				float distFromA = a.sub(testPoint).magnitude();
				float distFromB = b.sub(testPoint).magnitude();
				float perpendicularDistance = Math.abs(testPoint.sub(a).normalizedProjection(edgeVector.perpendicular()));
				
				float minTestPointDistance = Math.min(distFromA, distFromB);
				
				if ((Math.abs(testPoint.sub(a).normalizedProjection(edgeVector)) < edgeVector.magnitude()) &&
						(Math.abs(testPoint.sub(b).normalizedProjection(edgeVector)) < edgeVector.magnitude())) {
					minTestPointDistance = perpendicularDistance;
				}
				
				if (minTestPointDistance < minDistance) {
					minDistance = minTestPointDistance;
					minPoint = testPoint;
				}
			}
		}
		
		return minPoint;
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
		
		Vector2D closingVelocity = body1.getVelocity().sub(body2.getVelocity());
		float combinedInverseMass = (1 / body1.getMass()) + (1 / body2.getMass());
		
		float impulse = -(1 + PhysicsEngine.coefficientOfRestitution);
		impulse = impulse * closingVelocity.dot(collisionNormal);
		impulse = impulse / collisionNormal.dot(collisionNormal);
		impulse = impulse / (combinedInverseMass);
		
		return impulse;
	}
	
	/*
	 * Calculates the separating vector which resolves the overlapping of a onto b.
	 * The result takes into account the velocities of the two bodies
	 * 
	 */
	
	public static Vector2D calculateSeparatingVector(RigidBody a, RigidBody b) {
		Vector2D closingVelocity = a.getVelocity().sub(b.getVelocity());
		
		PolyBody body1 = (PolyBody)a;
		PolyBody body2 = (PolyBody)b;
		
		float b1Min = Float.POSITIVE_INFINITY;
		float b1Max = Float.NEGATIVE_INFINITY;
		float b2Min = Float.POSITIVE_INFINITY;
		float b2Max = Float.NEGATIVE_INFINITY;
		
		List<Vector2D> b1Points = body1.getPoints();
		List<Vector2D> b2Points = body2.getPoints();
		
		for (Vector2D point : b1Points) {
			float b1PointProjectionOnNormal = point.normalizedProjection(closingVelocity);
			if (b1PointProjectionOnNormal < b1Min) {
				b1Min = b1PointProjectionOnNormal;
			}
			if (b1PointProjectionOnNormal > b1Max) {
				b1Max = b1PointProjectionOnNormal;
			}
		}
		
		for (Vector2D point : b2Points) {
			float b2PointProjectionOnNormal = point.normalizedProjection(closingVelocity);
			if (b2PointProjectionOnNormal < b2Min) {
				b2Min = b2PointProjectionOnNormal;
			}
			if (b2PointProjectionOnNormal > b2Max) {
				b2Max = b2PointProjectionOnNormal;
			}
		}
		
		if ((b1Max <= b2Min) || (b2Max <= b1Min)) {
			System.out.println("Error: calculateResolutionVector called when the two bodies are not overlapping");
			return null;
		} else {
			float smallestMax = Math.min(b1Max, b2Max);
			float biggestMin = Math.max(b1Min, b2Min);
			float resolutionMagnitude = smallestMax - biggestMin;
			return closingVelocity.normalize().mult(-resolutionMagnitude);
		}
	}
}
