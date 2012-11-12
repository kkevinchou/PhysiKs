package physiks.engine;

import java.util.List;

import physiks.PhysiKsSim;
import physiks.collision.SatResult;
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
			if (body.getId() == 0) {
				System.out.println(body.getPosition());
			}
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
			Vector2D separatingVector = PhysHelper.calculateSeparatingVector(body, target);
			
			// GOAL: rewrite calculateSeparatingVector to not require this line
			separatingVector = Vector2D.UP.mult(PhysHelper.overlapAlongAxis(body, target, Vector2D.UP));
			
			body.setPosition(body.getPosition().add(separatingVector));
			
			if (body.getId() == 0) {
//				System.out.println(body.getPosition());
			}
			
			Vector2D collisionNormal = separatingVector.normalize();
			collisionNormal = new Vector2D(0, -1);
			collisionNormal = collisionNormal.normalize();
			
			float impulse = PhysHelper.calculateImpulseMagnitude(body, target, collisionNormal);
			
			Vector2D impulseVector = collisionNormal.mult(impulse);
			body.setVelocity(body.getVelocity().add(impulseVector.div(body.getMass())));
			target.setVelocity(target.getVelocity().sub(impulseVector.div(target.getMass())));
		}
		
		if (body.getId() == 0 && body.getPosition().getY() >= 380) {
			int asdf;
			asdf = 0;
		}
	}
}
