package engine;

import java.util.List;

import collision.SeparatingAxisTest;


import entities.RigidBody;
import forces.*;
import geometry.Vector2D;

public class PhysicsEngine {
	private List<RigidBody> entities;
	private List<Force> forces;
	
	public PhysicsEngine(List<RigidBody> entities) {
		this.entities = entities;
	}
	
	public void update(int delta) {
		Vector2D previousPosition;
		Vector2D previousVelocity;
		
		float deltaInSeconds = (float)delta/1000;
		
		for (RigidBody body : entities) {
			previousPosition = body.getPosition();
			previousVelocity = body.getVelocity();
			
			body.setPosition(previousPosition.add(body.getVelocity().mult(deltaInSeconds)));
			body.setVelocity(previousVelocity.add(body.getAcceleration().mult(deltaInSeconds)));

			body.clearForces();
//			body.addForce(new Gravity(body));
			if (body.getId() == 0) {
				body.addForce(new Poke(new Vector2D(1, -1), 2000));
			}
			
			Vector2D netForce = body.calculateNetForce();
			Vector2D acceleration = netForce.div(body.getMass());
			body.setAcceleration(acceleration);
			
			for (RigidBody target : entities) {
				if (body.getId() == target.getId()) continue;
				Vector2D separatingAxis = SeparatingAxisTest.findSeparatingAxis(body, target);
				if (separatingAxis == null) {
					body.setPosition(previousPosition);
					body.setVelocity(previousVelocity);
					
					separatingAxis = SeparatingAxisTest.findSeparatingAxis(body, target);
					Vector2D collisionNormal = separatingAxis.perpendicular().normalize();

					Vector2D bVelocityAlongNormal = collisionNormal.mult(body.getVelocity().dot(collisionNormal));
					Vector2D tVelocityAlongNormal = collisionNormal.mult(-target.getVelocity().dot(collisionNormal));
					
					body.setVelocity(body.getVelocity().sub(bVelocityAlongNormal));
					target.setVelocity(target.getVelocity().sub(tVelocityAlongNormal));
					
					float closingVelocity = bVelocityAlongNormal.sub(tVelocityAlongNormal).magnitude();
					
					Vector2D impulseVelocity = collisionNormal.mult(closingVelocity * (body.getMass() / (body.getMass() + target.getMass())));
					body.setVelocity(body.getVelocity().add(impulseVelocity));
				}
			}
		}
	}
	
	private boolean collidesWith(RigidBody a, RigidBody b) {
		return false;
	}
}
