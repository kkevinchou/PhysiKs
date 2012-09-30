package bits.component;

import bits.entity.*;
import bits.geometry.Vector2D;
import bits.steeringbehavior.Wander;

public class SteerComponent {
	private Bit entity;
	private Vector2D position;
	private Vector2D velocity;
	private Vector2D acceleration;
	private Vector2D heading;
	
	private float maxVelocity;
	
	private Wander wanderBehavior;
	
	public SteerComponent(Bit entity) {
		this.entity = entity;

		position = new Vector2D(entity.getX(), entity.getY());
		velocity = new Vector2D();
		acceleration = new Vector2D();
		
		/*
		double randomHeading = Random.getInstance().nextDouble(Math.PI);
		heading = new Vector2D(randomHeading * Math.cos(randomHeading), randomHeading * Math.sin(randomHeading));
		heading = heading.normalize();
		*/
		
		heading = new Vector2D(0, -1);
		heading = heading.normalize();
		
		wanderBehavior = new Wander(100f, (float)Math.toRadians(25), 30f);
		maxVelocity = 100;
	}
	
	public Vector2D getHeading() {
		return this.heading;
	}
	
	public void update(int dt) {
		float dtInSeconds = (float)dt / 1000;

		position = entity.getPosition();
		acceleration = new Vector2D(0, 0);
		
		Vector2D wanderAccel = wanderBehavior.calculateSteeringAccel(position, heading);
		
		acceleration = acceleration.add(wanderAccel);
		
		Vector2D deltaVelocity = acceleration.mult(dtInSeconds);
		velocity = velocity.add(deltaVelocity);
		
		if (velocity.magnitude() > maxVelocity) {
			velocity = velocity.normalize();
			velocity = velocity.mult(maxVelocity);
		}
		
		Vector2D deltaPosition = velocity.mult(dtInSeconds);
		position = position.add(deltaPosition);
		
		heading = velocity.normalize();
		entity.setPosition(position);
	}
}
