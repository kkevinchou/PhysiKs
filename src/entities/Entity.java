package entities;
import geometry.Vector2D;
import util.IntGenerator;

public class Entity {
	private Vector2D position;
	private Vector2D velocity;
	private Vector2D acceleration;
	
	private float radius;
	
	private final int id = intGenerator.nextInt();
	private static final IntGenerator intGenerator  = new IntGenerator();
	
	public Entity(float x, float y, float radius) {
		position = new Vector2D(x, y);
		velocity = new Vector2D(0, 0);
		acceleration = new Vector2D(0, 0);
		this.radius = radius;
	}
	
	public int getId() {
		return id;
	}

	public Vector2D getPosition() {
		return position;
	}
	
	public void setPosition(final Vector2D position) {
		this.position = new Vector2D(position.getX(), position.getY());
	}
	
	public float getRadius() {
		return radius;
	}
}
