package physiks.forces;

import physiks.geometry.Vector2D;

public class Poke implements Force {
	private Vector2D force;
	
	public Poke(Vector2D direction, float strength) {
		force = direction.normalize().mult(strength);
	}

	public Vector2D calculateForce() {
		return force;
	}

}
