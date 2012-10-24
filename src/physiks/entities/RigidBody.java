package physiks.entities;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import physiks.forces.Force;
import physiks.geometry.Rectangle;
import physiks.geometry.Vector2D;
import physiks.util.IntGenerator;



public abstract class RigidBody {
	private float mass;
	private Vector2D position;
	private Vector2D velocity;
	private Vector2D acceleration;
	private VisualData visualData;
	
	private List<Force> forces;
	
	private final int id = intGenerator.nextInt();
	private static final IntGenerator intGenerator  = new IntGenerator();
	private List<Color> colors;
	
	private Color generateRandomColor() {
		int randomOffset = (int)(Math.random() * colors.size());
		return colors.get(randomOffset);
	}
	
	public RigidBody(float x, float y, float mass) {
		this.mass = mass;
		position = new Vector2D(x, y);
		velocity = new Vector2D(0, 0);
		acceleration = new Vector2D(0, 0);
		
		forces = new ArrayList<Force>();
		
		// Color palette, should move this to a separate static class later
		colors = new ArrayList<Color>();
		colors.add(new Color(245, 162, 63));
		colors.add(new Color(168, 112, 44));
		colors.add(new Color(109, 190, 194));
		colors.add(new Color(255, 245, 81));
		colors.add(new Color(215, 20, 20));
		colors.add(new Color(225, 123, 10));
		
		visualData = new VisualData();
		visualData.setColor(generateRandomColor());
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
	
	public VisualData getVisualComponent() {
		return visualData;
	}
	
	public abstract Rectangle getAABoundingBox();
	
	public void clearForces() {
		forces.clear();
	}
}