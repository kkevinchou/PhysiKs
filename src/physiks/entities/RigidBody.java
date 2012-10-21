package physiks.entities;
import java.util.ArrayList;
import java.util.List;

import physiks.forces.Force;
import physiks.geometry.Vector2D;
import physiks.util.IntGenerator;



public abstract class RigidBody {
	private float mass;
	private Vector2D position;
	private Vector2D velocity;
	private Vector2D acceleration;
	
	private List<Force> forces;
	
	private final int id = intGenerator.nextInt();
	private static final IntGenerator intGenerator  = new IntGenerator();
	
	public RigidBody(float x, float y, float mass) {
		this.mass = mass;
		position = new Vector2D(x, y);
		velocity = new Vector2D(0, 0);
		acceleration = new Vector2D(0, 0);
		
		forces = new ArrayList<Force>();
	}
	
	public int getId() {
		return id;
	}

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(final Vector2D position) {
		this.position = position;
	}
	
	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public Vector2D getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector2D acceleration) {
		this.acceleration = acceleration;
	}
	
	public float getMass() {
		return mass;
	}
	
	public void addForce(Force f) {
		forces.add(f);
	}
	
	public Vector2D calculateNetForce() {
		Vector2D netForce = new Vector2D(0, 0);
		
		for (Force f : forces) {
			netForce = netForce.add(f.calculateForce());
		}
		
		return netForce;
	}
	
	public void clearForces() {
		forces.clear();
	}
}