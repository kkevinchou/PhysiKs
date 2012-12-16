package physiks.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import physiks.audio.AudioPlayer;
import physiks.collision.SeparatingAxisTest;
import physiks.engine.misc.PhysHelper;
import physiks.engine.misc.SpatialData;
import physiks.entities.PolyBody;
import physiks.entities.RigidBody;
import physiks.forces.*;
import physiks.geometry.Vector2D;
import physiks.main.PhysiKsSim;
import physiks.quadtree.QuadTree;

public class PhysicsEngine {
	private List<RigidBody> entities;
	private QuadTree quadTree;
	private Stack<Map<Integer, SpatialData>> frames;
//	private int frameNumber = 0;
	
	public static final float coefficientOfRestitution = 0.7f;
	
	public PhysicsEngine(List<RigidBody> entities) {
		this.entities = entities;
		frames = new Stack<Map<Integer, SpatialData>>();
		quadTree = new QuadTree(-PhysiKsSim.WIDTH, -PhysiKsSim.HEIGHT, PhysiKsSim.WIDTH*3, PhysiKsSim.HEIGHT*3, 4000);
	}
	
	public void stepBack() {
		if (frames.size() == 0) return;
		
		Map<Integer, SpatialData> frame = frames.pop();
		
		for (RigidBody entity : entities) {
			SpatialData s = frame.get(entity.getId());
			if (s != null) {
				s.loadInto(entity);
			}
		}
	}
	
	public void update(int delta) {
//		System.out.println("   *** Frame Number: " + frameNumber++);
		delta = (PhysiKsSim.MODE == PhysiKsSim.Mode.Normal) ? 16 : delta;
		
		float deltaInSeconds = (float)delta/1000;
		
		quadTree.clear();
		for (RigidBody entity : entities) {
			quadTree.add(entity);
		}
		
		for (RigidBody entity : entities) {
			performTimeStep(entity, deltaInSeconds);
		}

		Map<Integer, SpatialData> m = new HashMap<Integer, SpatialData>();
		for (RigidBody entity : entities) {
			m.put(entity.getId(), SpatialData.createFrom(entity));
		}
		frames.push(m);
		
//		String logOutput = frameNumber++ + " " + entities.get(0).getPosition() + " " + entities.get(0).getVelocity() + " " + entities.get(0).getAcceleration();
//		System.out.println(logOutput);
	}
	
	private void performTimeStep(RigidBody body, float delta) {
		PhysHelper.zeroOutMicroVelocities(body, 0.01f);
		
		if (body.getMass() == Float.POSITIVE_INFINITY) {
			return;
		}
		
		SpatialData prevSpatialData = SpatialData.createFrom(body);
		advanceBody(body, delta);
		
		List<RigidBody> collisionCandidates = quadTree.getIntersectionCandidates(body);
		for (RigidBody target : collisionCandidates) {
			if (collidesWidth(body, target)) {
				resolveCollision(body, target, prevSpatialData);
				AudioPlayer.getInstance().playWav("bounce");
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
		
		Vector2D separatingAxis = SeparatingAxisTest.getSeparatingAxis(body, target);

		return (separatingAxis == null);
	}
	
	private void resolveCollision(RigidBody a, RigidBody b, SpatialData prevSpatialData) {
		PolyBody body = (PolyBody)a;
		PolyBody target = (PolyBody)b;
		
		if (body.getId() == PhysiKsSim.testId && target.getMass() != Float.POSITIVE_INFINITY && PhysiKsSim.testDebug) {
			PhysiKsSim.testDebug = false;
		}
		
		Vector2D collisionNormal = calculateCollisionNormal(body, target, prevSpatialData);
		Vector2D separatingVector = calculateSeparatingVector(body, target, collisionNormal);

//		Vector2D separatingVector = PhysHelper.calculateMinimumSeparatingVector(body, target);
//		Vector2D collisionNormal = separatingVector.normalize();
		
		body.setPosition(body.getPosition().add(separatingVector));
		
		float impulseMagnitude = PhysHelper.calculateImpulseMagnitude(body, target, collisionNormal);
		Vector2D impulseVector = collisionNormal.mult(impulseMagnitude);
		
		body.setVelocity(body.getVelocity().add(impulseVector.div(body.getMass())));
		target.setVelocity(target.getVelocity().sub(impulseVector.div(target.getMass())));
	}
	
	private Vector2D calculateCollisionNormal(RigidBody a, RigidBody b, SpatialData prevSpatialData) {
		PolyBody body1 = (PolyBody)a;
		PolyBody body2 = (PolyBody)b;
		
		Vector2D collisionNormal = null;
		
		SpatialData currentSpatialData = SpatialData.createFrom(body1);
		
		// Rewind body for separating axis calculations
		prevSpatialData.loadInto(body1);
		Vector2D separatingAxis = SeparatingAxisTest.getSeparatingAxis(body1, body2);
		
		// Fall-back: Use the minimum separating vector as the normal
		if (separatingAxis == null) {
			Vector2D minSeparatingVector = PhysHelper.calculateMinimumSeparatingVector(body1, body2);
			collisionNormal = minSeparatingVector.normalize();
		} else {
			// TODO: Make sure the collision normal is actually the normal of the closest edge.
			collisionNormal = separatingAxis.perpendicular();
			collisionNormal = collisionNormal.pointAlongWith(body1.getCenter().sub(body2.getCenter())).normalize();
			
			// Reload current position
			body1.setPosition(currentSpatialData.getPosition());
		}
		
		// debug
		Vector2D minSeparatingVector = PhysHelper.calculateMinimumSeparatingVector(body1, body2);
		collisionNormal = collisionNormal.pointAlongWith(minSeparatingVector);
		
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
}
