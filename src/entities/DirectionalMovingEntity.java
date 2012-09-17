package entities;

import kgeometry.V2D;

public class DirectionalMovingEntity extends TargettedMovingEntity {
	private float maxDistance = 200;
	private boolean reachedMaxDistance = false;
	private boolean currentDistanceReached = 0;

	protected DirectionalMovingEntity(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public DirectionalMovingEntity() {
		this(0, 0, 0, 0);
	}
	
	public void update(float dt) {
		super.update(dt);
	}
	
	public void setDirection(V2D target) {
		setTarget(target.scale(maxDistance));
	}
}
