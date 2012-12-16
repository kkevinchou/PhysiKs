package physiks.engine.misc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import physiks.collision.SeparatingAxisTest;
import physiks.engine.PhysicsEngine;
import physiks.entities.PolyBody;
import physiks.entities.RigidBody;
import physiks.geometry.Vector2D;
import physiks.util.Util;

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
			
			if (Util.epsilonEquals(overlap, 0, 0.001f)) {
				return 0;
			}
			
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
	 * @effect Sets the velocity component to zero if it is below a certain threshold
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
		// Formula and explanation: http://chrishecker.com/images/e/e7/Gdmphys3.pdf
		
		Vector2D closingVelocity = body1.getVelocity().sub(body2.getVelocity());
		
		float combinedInverseMass = (1 / body1.getMass()) + (1 / body2.getMass());
		
		float impulse = -(1 + PhysicsEngine.coefficientOfRestitution);
		impulse = impulse * closingVelocity.dot(collisionNormal);
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
	
	/**
	 * @param a
	 * @param b
	 * @return minimum separating vector, a zero vector is returned if the two bodies are not intersecting
	 */
	public static Vector2D calculateMinimumSeparatingVector(RigidBody a, RigidBody b) {
		PolyBody body1 = (PolyBody)a;
		PolyBody body2 = (PolyBody)b;

		float minOverlap = Float.POSITIVE_INFINITY;
		Vector2D minSeparatingVector = Vector2D.ZERO;
		
		if (SeparatingAxisTest.getSeparatingAxis(body1, body2) == null) {
			List<Vector2D> normals = getUniqueNormals(body1, body2);
			
			for (Vector2D normal : normals) {
				float overlap = PhysHelper.overlapAlongAxis(body1, body2, normal);
				if (overlap < minOverlap) {
					minOverlap = overlap;
					minSeparatingVector = normal.mult(minOverlap).pointAlongWith(body1.getCenter().sub(body2.getCenter()));
				}
			}
		}

		return minSeparatingVector;
	}
	
	public static List<Vector2D> getUniqueNormals(PolyBody body1, PolyBody body2) {
		Map<Float, Vector2D> normals = new LinkedHashMap<Float, Vector2D>();
		
		for (Vector2D normal : body1.getNormals()) {
			Float key = normal.getY() / normal.getX();
			normals.put(key, normal);
		}
		
		for (Vector2D normal : body2.getNormals()) {
			Float key = normal.getY() / normal.getX();
			normals.put(key, normal);
		}
		
		return new ArrayList<Vector2D>(normals.values());
	}
}
