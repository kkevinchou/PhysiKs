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
	private static final float coefficientOfRestitution = 0.7f;
	
	public PhysicsEngine(List<RigidBody> entities) {
		this.entities = entities;
	}
	
	public void update(int delta) {
		delta = Math.min(16, delta);
		
		float deltaInSeconds = (float)delta/1000;
		
		for (RigidBody body : entities) {
			body.setPosition(body.getPosition().add(body.getVelocity().mult(deltaInSeconds)));
			body.setVelocity(body.getVelocity().add(body.getAcceleration().mult(deltaInSeconds)));
			
			body.clearForces();
			
			if (body.getMass() != Float.POSITIVE_INFINITY) {
				body.addForce(new Gravity(body));
			}
			
			Vector2D netForce = body.calculateNetForce();
			Vector2D acceleration = netForce.div(body.getMass());
			body.setAcceleration(acceleration);
				
			for (RigidBody target : entities) {
				if (body.getId() == target.getId()) continue;
				
				SatResult satResult = SeparatingAxisTest.getSatResult(body, target);

				Vector2D separatingVector = satResult.getMinimumSeparatingVector();
				if (separatingVector != null) {
					body.setPosition(body.getPosition().add(separatingVector));
					
					Vector2D collisionNormal = separatingVector.normalize();
					float impulse = calculateImpulseMagnitude(body, target, collisionNormal);
					
					Vector2D impulseVector = collisionNormal.mult(impulse);
					body.setVelocity(body.getVelocity().add(impulseVector.div(body.getMass())));
					target.setVelocity(target.getVelocity().sub(impulseVector.div(target.getMass())));
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
