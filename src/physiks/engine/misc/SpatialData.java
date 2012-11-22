package physiks.engine.misc;

import physiks.entities.RigidBody;
import physiks.geometry.Vector2D;

public class SpatialData {
	Vector2D position;
	Vector2D velocity;
	Vector2D acceleration;
	
	public SpatialData(RigidBody body) {
		this.position = body.getPosition();
		this.velocity = body.getVelocity();
		this.acceleration = body.getAcceleration();
	}
	
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
