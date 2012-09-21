package bits.entity;

import bits.component.*;

public class Entity {
	private float x;
	private float y;
	
	public Entity() {
		this.x = 100;
		this.y = 100;
	}
	
	public void update() {
		
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
