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
			PhysHelper.zeroOutMicroVelocities(body, 0.01f);

			Vector2D velocity = body.getVelocity();
			
			body.setPosition(body.getPosition().add(velocity.mult(deltaInSeconds)));
			body.setVelocity(body.getVelocity().add(body.getAcceleration().mult(deltaInSeconds)));
			
			body.clearForces();
			if (body.getMass() != Float.POSITIVE_INFINITY) {
				body.addForce(new Gravity(body));
			}

			Vector2D netForce = body.calculateNetForce();
			Vector2D acceleration = netForce.div(body.getMass());
			body.setAcceleration(acceleration);
			
			List<RigidBody> collisionCandidates = quadTree.getIntersectionCandidates(body);
			
			for (RigidBody t : collisionCandidates) {
				PolyBody target = (PolyBody)t;
				if (body.getId() == target.getId()) continue;
				if (body.getMass() == Float.POSITIVE_INFINITY) continue;
				
				SatResult satResult = SeparatingAxisTest.getSatResult(body, target);

				Vector2D separatingAxis = satResult.getSeparatingAxis();
				if (separatingAxis == null) {
					Vector2D separatingVector = PhysHelper.calculateSeparatingVector(body, target);
//					separatingVector = Vector2D.UP.mult(PhysHelper.overlapAlongAxis(body, target, Vector2D.UP));
					body.setPosition(body.getPosition().add(separatingVector));
					
					Vector2D collisionNormal = separatingVector.normalize();
//					collisionNormal = new Vector2D(0, -1);
					collisionNormal = collisionNormal.normalize();
					
					float impulse = PhysHelper.calculateImpulseMagnitude(body, target, collisionNormal);
					
					Vector2D impulseVector = collisionNormal.mult(impulse);
					body.setVelocity(body.getVelocity().add(impulseVector.div(body.getMass())));
					target.setVelocity(target.getVelocity().sub(impulseVector.div(target.getMass())));
				}
			}
		}
	}
	
}
