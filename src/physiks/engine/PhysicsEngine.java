package physiks.engine;

import java.util.List;

import physiks.PhysiKsSim;
import physiks.collision.SeparatingAxisTest;
import physiks.entities.PolyBody;
import physiks.entities.RigidBody;
import physiks.forces.*;
import physiks.geometry.Vector2D;
import physiks.quadtree.QuadTree;

public class PhysicsEngine {
	private List<RigidBody> entities;
	private QuadTree quadTree;
	public static final float coefficientOfRestitution = 0.7f;
	
	public PhysicsEngine(List<RigidBody> entities) {
		this.entities = entities;
		quadTree = new QuadTree(-PhysiKsSim.WIDTH, -PhysiKsSim.HEIGHT, PhysiKsSim.WIDTH*3, PhysiKsSim.HEIGHT*3);
	}
	
	public void update(int delta) {
		delta = Math.min(16, delta);
		float deltaInSeconds = (float)delta/1000;
		
		quadTree.clear();
		for (RigidBody entity : entities) {
			quadTree.add(entity);
		}

		for (RigidBody b : entities) {
			PolyBody body = (PolyBody)b;
			performTimeStep(body, deltaInSeconds);
		}
	}
	
	public class SpatialData {
		Vector2D position;
		Vector2D velocity;
		Vector2D acceleration;
		
		public SpatialData(Vector2D position, Vector2D velocity, Vector2D acceleration) {
			this.position = position;
			this.velocity = velocity;
			this.acceleration = acceleration;
		}
		
		public Vector2D getPosition() {
			return position;
		}

		public Vector2D getVelocity() {
			return velocity;
		}

		public Vector2D getAcceleration() {
			return acceleration;
		}
	}
	
	private void performTimeStep(RigidBody body, float delta) {
		PhysHelper.zeroOutMicroVelocities(body, 0.01f);
		
		Vector2D prevPosition = body.getPosition();
		Vector2D prevVelocity = body.getVelocity();
		Vector2D prevAcceleratioin = body.getAcceleration();
		
		SpatialData prevSpatialData = new SpatialData(prevPosition, prevVelocity, prevAcceleratioin);
		
		advanceBody(body, delta);
		
		List<RigidBody> collisionCandidates = quadTree.getIntersectionCandidates(body);
		for (RigidBody target : collisionCandidates) {
			checkForCollision(body, target, prevSpatialData);
		}
	}
	
	private void advanceBody(RigidBody body, float delta) {
		Vector2D position = body.getPosition();
		Vector2D velocity = body.getVelocity();
		Vector2D acceleration = body.getAcceleration();
		
		body.setPosition(position.add(velocity.mult(delta)));
		body.setVelocity(velocity.add(acceleration.mult(delta)));
		
		body.clearForces();
		if (body.getMass() != Float.POSITIVE_INFINITY) {
			body.addForce(new Gravity(body));
		}

		Vector2D netForce = body.calculateNetForce();
		Vector2D newAcceleration = netForce.div(body.getMass());
		body.setAcceleration(newAcceleration);
	}
	
	private void checkForCollision(RigidBody a, RigidBody b, SpatialData prevSpatialData) {
		PolyBody body = (PolyBody)a;
		PolyBody target = (PolyBody)b;
		
		if (body.getId() == target.getId()) return;
		if (body.getMass() == Float.POSITIVE_INFINITY) return;
		
		Vector2D separatingAxis = SeparatingAxisTest.getSeparatingAxis(body, target);

		if (separatingAxis == null) {
			Vector2D collisionNormal = calculateCollisionNormal(body, target);
			Vector2D separatingVector = calculateSeparatingVector(body, collisionNormal, prevSpatialData);
			
			body.setPosition(body.getPosition().add(separatingVector));
			
			float impulse = PhysHelper.calculateImpulseMagnitude(body, target, collisionNormal);
			
			Vector2D impulseVector = collisionNormal.mult(impulse);
			body.setVelocity(body.getVelocity().add(impulseVector.div(body.getMass())));
			target.setVelocity(target.getVelocity().sub(impulseVector.div(target.getMass())));
		}
	}
	
	/*
	 * Calculates the separating vector which resolves the overlapping of a onto b.
	 * The result takes into account the velocities of the two bodies
	 * 
	 */
	
	public static Vector2D calculateCollisionNormal(RigidBody a, RigidBody b) {
		PolyBody body1 = (PolyBody)a;
		PolyBody body2 = (PolyBody)b;
		
		List<Vector2D> closestBody1Points = PhysHelper.getClosestPoints(body1, body2);
		
		Vector2D collisionNormal = null;
		
		if (closestBody1Points.size() == 2) {
			collisionNormal = closestBody1Points.get(0).sub(closestBody1Points.get(1)).perpendicular().normalize();
			if (body1.getVelocity().dot(collisionNormal) > 0) {
				collisionNormal = collisionNormal.mult(-1);
			}
		}
		
		return collisionNormal;
	}
	
	public static Vector2D calculateSeparatingVector(RigidBody body1, Vector2D collisionNormal, SpatialData prevSpatialData) {
		float separatingMagnitude = prevSpatialData.getPosition().sub(body1.getPosition()).dot(collisionNormal);
		Vector2D separatingVector = collisionNormal.mult(separatingMagnitude);
		
		return separatingVector;
	}
}
