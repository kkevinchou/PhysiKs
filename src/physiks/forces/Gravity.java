package physiks.forces;

import physiks.entities.RigidBody;
import physiks.geometry.Vector2D;

public class Gravity implements Force {
	RigidBody owner;
	
	public Gravity(RigidBody owner) {
		this.owner = owner;
	}

	public Vector2D calculateForce() {
		return new Vector2D(0, owner.getMass() * 150);
	}
}
