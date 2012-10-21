package physiks.engine;

import java.util.List;

import physiks.collision.CollisionChecker;
import physiks.collision.CollisionResult;
import physiks.collision.SatResult;
import physiks.collision.SeparatingAxisTest;
import physiks.entities.PolyBody;
import physiks.entities.RigidBody;
import physiks.forces.*;
import physiks.geometry.Vector2D;




public class PhysicsEngine {
	private List<RigidBody> entities;
	private List<Force> forces;
	private static final float coefficientOfRestitution = 0.7f;
	
	public PhysicsEngine(List<RigidBody> entities) {
		this.entities = entities;
	}
	
	public void update(int delta) {
		delta = Math.min(16, delta);
		
		float deltaInSeconds = (float)delta/1000;
		
		for (RigidBody b : entities) {
			PolyBody body = (PolyBody)b;
			Vector2D velocity = body.getVelocity();
			
			if (Math.abs(velocity.getX()) < 0.01f) {
				body.setVelocity(new Vector2D(0, velocity.getY()));
			}
			
			if (Math.abs(velocity.getY()) < 0.01f) {
				body.setVelocity(new Vector2D(velocity.getX(), 0));
			}
			
			body.setPosition(body.getPosition().add(velocity.mult(deltaInSeconds)));
			body.setVelocity(body.getVelocity().add(body.getAcceleration().mult(deltaInSeconds)));
			
			body.clearForces();
			
			if (body.getMass() != Float.POSITIVE_INFINITY) {
				body.addForce(new Gravity(body));
			}

			Vector2D netForce = body.calculateNetForce();
			Vector2D acceleration = netForce.div(body.getMass());
			body.setAcceleration(acceleration);

			for (RigidBody t : entities) {
				PolyBody target = (PolyBody)t;
				if (body.getId() == target.getId()) continue;
				if (body.getMass() == Float.POSITIVE_INFINITY) continue;
				
				SatResult satResult = SeparatingAxisTest.getSatResult(body, target);

				Vector2D separatingVector = satResult.getMinimumSeparatingVector();
				if (separatingVector != null) {
					body.setPosition(body.getPosition().add(separatingVector));
					
					Vector2D separatingAxis = SeparatingAxisTest.getSatResult(body, target).getSeparatingAxis();
					Vector2D collisionNormal = separatingVector.normalize();
					
					Vector2D closestTargetPointToBody = getClosestPoint(body, target);
					Vector2D closestBodyPointToTarget = getClosestPoint(target, body);
					float distFromClosestPoints = closestBodyPointToTarget.sub(closestTargetPointToBody).magnitude();
					
					if (distFromClosestPoints < 1) {
						collisionNormal = body.getVelocity().normalize().mult(-1);
					}
					
					float impulse = calculateImpulseMagnitude(body, target, collisionNormal);
					
					Vector2D impulseVector = collisionNormal.mult(impulse);
					body.setVelocity(body.getVelocity().add(impulseVector.div(body.getMass())));
					target.setVelocity(target.getVelocity().sub(impulseVector.div(target.getMass())));
					
					int a = 0;
				}
			}
		}
	}
	
	private Vector2D calculateResolutionVector(RigidBody a, RigidBody b) {
		Vector2D closingVelocity = a.getVelocity().sub(b.getVelocity());
		
		if ((a instanceof PolyBody) && (b instanceof PolyBody)) {
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
			} else {
				float smallestMax = Math.min(b1Max, b2Max);
				float biggestMin = Math.max(b1Min, b2Min);
				float resolutionMagnitude = smallestMax - biggestMin;
				return closingVelocity.normalize().mult(-resolutionMagnitude);
			}
		} else {
			System.out.println("Error: [PhysicsEngine.calculateResolutionVector] Non polybody arguments are not supported");
		}
		return null;
	}
	
	// Gets the point on body2 that is closest to body1
	private Vector2D getClosestPoint(PolyBody body1, PolyBody body2) {
		List<Vector2D> testPoints = body2.getPoints();
		List<Vector2D> edgePoints = body1.getPoints();
		
		Vector2D minPoint = null;
		float minDistance = Float.POSITIVE_INFINITY;
		
		for (int i = 0, size = edgePoints.size(); i < size; i++) {
			Vector2D a = edgePoints.get(i);
			Vector2D b = edgePoints.get((i + 1) % size);
			
			Vector2D edge = b.sub(a);
			
			for (Vector2D testPoint : testPoints) {
				float distFromA = a.sub(testPoint).magnitude();
				float distFromB = b.sub(testPoint).magnitude();
				float perpendicularDistance = Math.abs(testPoint.sub(a).normalizedProjection(edge.perpendicular()));
				
				float minTestPointDistance = Math.min(distFromA, distFromB);
				
				if ((Math.abs(testPoint.sub(a).normalizedProjection(edge)) < edge.magnitude()) &&
						(Math.abs(testPoint.sub(b).normalizedProjection(edge)) < edge.magnitude())) {
					minTestPointDistance = perpendicularDistance;
				}
				
				if (minTestPointDistance < minDistance) {
					minDistance = minTestPointDistance;
					minPoint = testPoint;
				}
			}
		}
		
		if (minPoint == null) {
			System.out.println("Error [PhysicsEngine:getClosestPoint] minPoint was unexpectedly found to be null");
		}
		
		return minPoint;
	}
	
	private float calculateImpulseMagnitude(RigidBody body1, RigidBody body2, Vector2D collisionNormal) {
		// Formula: http://chrishecker.com/images/e/e7/Gdmphys3.pdf
		
		Vector2D closingVelocity = body1.getVelocity().sub(body2.getVelocity());
		float combinedInverseMass = (1 / body1.getMass()) + (1 / body2.getMass());
		
		float impulse = -(1 + PhysicsEngine.coefficientOfRestitution);
		impulse = impulse * closingVelocity.dot(collisionNormal);
		impulse = impulse / collisionNormal.dot(collisionNormal);
		impulse = impulse / (combinedInverseMass);
		
		return impulse;
	}
}
