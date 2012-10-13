package engine;

import java.util.List;

import collision.CollisionChecker;
import collision.CollisionResult;
import collision.SatResult;
import collision.SeparatingAxisTest;


import entities.RigidBody;
import forces.*;
import geometry.Vector2D;

public class PhysicsEngine {
	private List<RigidBody> entities;
	private List<Force> forces;
	private static final float coefficientOfRestitution = 1;
	
	public PhysicsEngine(List<RigidBody> entities) {
		this.entities = entities;
	}
	
	public void update(int delta) {
		float deltaInSeconds = (float)delta/1000;
		
		for (RigidBody body : entities) {
			body.setPosition(body.getPosition().add(body.getVelocity().mult(deltaInSeconds)));
			body.setVelocity(body.getVelocity().add(body.getAcceleration().mult(deltaInSeconds)));
			
			Vector2D netForce = body.calculateNetForce();
			Vector2D acceleration = netForce.div(body.getMass());
			body.setAcceleration(acceleration);
				
			for (RigidBody target : entities) {
				if (body.getId() == target.getId()) continue;
				CollisionResult collisionResult = CollisionChecker.check(body, target);
				if (collisionResult.hasCollision()) {
					Vector2D separatingVector = collisionResult.getMinimumSeparatingVector();
					body.setPosition(body.getPosition().add(separatingVector));
					
					Vector2D collisionNormal = collisionResult.getCollisionNormal();
					float impulse = calculateImpulseMagnitude(body, target, collisionNormal);
					
					Vector2D impulseVector = collisionNormal.mult(impulse);
					body.setVelocity(body.getVelocity().add(impulseVector.div(body.getMass())));
					target.setVelocity(body.getVelocity().sub(impulseVector.div(target.getMass())));
				}
			}
		}
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
