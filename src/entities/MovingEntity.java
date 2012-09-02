package entities;

import kgeometry.V2D;

public class MovingEntity extends Entity {
	private V2D target;
	private float speed = 300; // pixels per frame
	
	public MovingEntity(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public MovingEntity() {
		this(0, 0, 0, 0);
	}

	public void update(float dt) {
		if (target == null) return;
		
		V2D position = new V2D(x, y);
		V2D vToTarget = target.sub(position);
		float distance = vToTarget.magnitude();
		float delta = speed * dt;
		
		if (distance == 0) {
			return;
		}
		
		if (distance < delta) {
			delta = distance;
		}
		
		V2D vMovement = vToTarget.normalizeAndScale(delta);
		V2D newPosition = position.add(vMovement);
		
		setPosition(newPosition);	
	}
	
	public void setTarget(V2D target) {
		this.target = target;
	}
	
	private void setPosition(V2D position) {
		x = position.getX();
		y = position.getY();
	}
}