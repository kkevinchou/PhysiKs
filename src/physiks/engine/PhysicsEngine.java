package physiks.engine;

import java.util.List;

import physiks.PhysiKsSim;
import physiks.collision.SeparatingAxisTest;
import physiks.engine.misc.PhysHelper;
import physiks.engine.misc.SpatialData;
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
	
	private void performTimeStep(RigidBody body, float delta) {
		PhysHelper.zeroOutMicroVelocities(body, 0.01f);
		SpatialData prevSpatialData = new SpatialData(body);
		
		advanceBody(body, delta);
		
		List<RigidBody> collisionCandidates = quadTree.getIntersectionCandidates(body);
		for (RigidBody target : collisionCandidates) {
			if (collidesWidth(body, target)) {
				resolveCollision(body, target, prevSpatialData);				
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
	
	private boolean collidesWidth(RigidBody a, RigidBody b) {
		PolyBody body = (PolyBody)a;
		PolyBody target = (PolyBody)b;
		
		if (body.getId() == target.getId()) return false;
		if (body.getMass() == Float.POSITIVE_INFINITY) return false;
		
		Vector2D separatingAxis = SeparatingAxisTest.getSeparatingAxis(body, target);

		return (separatingAxis == null);
	}
	
	private void resolveCollision(RigidBody a, RigidBody b, SpatialData prevSpatialData) {
		PolyBody body = (PolyBody)a;
		PolyBody target = (PolyBody)b;
		
		Vector2D collisionNormal = calculateCollisionNormal(body, target, prevSpatialData);
		Vector2D separatingVector = calculateSeparatingVector(body, target, collisionNormal);
		
		body.setPosition(body.getPosition().add(separatingVector));
		
		float impulse = PhysHelper.calculateImpulseMagnitude(body, target, collisionNormal);
		
		Vector2D impulseVector = collisionNormal.mult(impulse);
		body.setVelocity(body.getVelocity().add(impulseVector.div(body.getMass())));
		target.setVelocity(target.getVelocity().sub(impulseVector.div(target.getMass())));
	}
	
	public static Vector2D calculateCollisionNormal(RigidBody a, RigidBody b, SpatialData prevSpatialData) {
		PolyBody body1 = (PolyBody)a;
		PolyBody body2 = (PolyBody)b;
		
		SpatialData currentSpatialData = new SpatialData(a);
		
		// Load previous position to find the separating axis
		body1.setPosition(prevSpatialData.getPosition());
		
		Vector2D separatingAxis = SeparatingAxisTest.getSeparatingAxis(body1, body2);
		if (separatingAxis == null) {
			System.out.println("WTF? no separating axis after rewinding it?");
			return null;
		}
		
		// TODO: Make sure the collision normal is actually the normal of the closest edge.
		Vector2D collisionNormal = separatingAxis.perpendicular();
		collisionNormal = collisionNormal.pointAlongWith(body1.getVelocity().mult(-1)).normalize();
		
		// Reload current position
		body1.setPosition(currentSpatialData.getPosition());
		
		return collisionNormal;
	}
	
	/*
	 * Calculates the separating vector which resolves the overlapping of a onto; b.
	 * The result takes into account the velocities of the two bodies
	 * 
	 */

	// TODO: the separating vector should rewind based on the velocity, then play more
	// physics in the axis that doesn't collide.
	private Vector2D calculateSeparatingVector(RigidBody body1, RigidBody body2, Vector2D collisionNormal) {
		
		float separatingMagnitude = PhysHelper.overlapAlongAxis(body1, body2, collisionNormal);
		Vector2D separatingVector = collisionNormal.mult(separatingMagnitude);

		return separatingVector;
	}
	
	private void loadSpatialDataIntoRigidBody(RigidBody body, SpatialData spatialData) {
		body.setPosition(spatialData.getPosition());
		body.setVelocity(spatialData.getVelocity());
		body.setAcceleration(spatialData.getAcceleration());
	}
}
