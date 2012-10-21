package physiks.collision;

import physiks.geometry.Vector2D;

public final class CollisionResult {
	private Vector2D minimumSeparatingVector;
	private Vector2D collisionNormal;
	
	public CollisionResult() {
		minimumSeparatingVector = null;
		collisionNormal = null;
	}
	
	public boolean hasCollision() {
		return (minimumSeparatingVector != null);
	}

	public Vector2D getMinimumSeparatingVector() {
		return minimumSeparatingVector;
	}

	public  void setMinimumSeparatingVector(Vector2D minimumSeparatingVector) {
		this.minimumSeparatingVector = minimumSeparatingVector;
	}

	public final Vector2D getCollisionNormal() {
		return collisionNormal;
	}

	public void setCollisionNormal(Vector2D collisionNormal) {
		this.collisionNormal = collisionNormal;
	}
}
