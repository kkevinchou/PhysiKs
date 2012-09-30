package bits.steeringbehavior;

import bits.geometry.Vector2D;
import bits.util.Debug;
import bits.util.Random;

public class Wander {
	private Random r;
	private float wanderDistance;
	private float maxWanderAngleDelta;
	private float wanderRadius;
	private float wanderAngle;
	private float wanderStrength;
	
	public Wander(float wanderDistance, float maxWanderAngleDelta, float wanderRadius) {
		r = Random.getInstance();
		this.wanderDistance = wanderDistance;
		this.maxWanderAngleDelta = maxWanderAngleDelta;
		this.wanderRadius = wanderRadius;
		
		Debug.getInstance().setData("wanderRadius", wanderRadius);
		
		// Initialize wander angle to be directly in front
		wanderAngle = (float)(3 * Math.PI / 2);
		wanderStrength = 300;
	}
	
	public Vector2D calculateSteeringAccel(Vector2D position, Vector2D heading) {
		Vector2D circlePosition = heading.mult(wanderDistance);
		float wanderAngleDelta = r.floatInRange(-maxWanderAngleDelta, maxWanderAngleDelta);
		
		wanderAngle += wanderAngleDelta;

		Vector2D wanderPoint = new Vector2D(wanderRadius * Math.cos(wanderAngle), wanderRadius * Math.sin(wanderAngle));
		
		if (Math.abs(wanderPoint.getX()) < 0.00001f) {
			wanderPoint = new Vector2D(0, wanderPoint.getY());
		}
		if (Math.abs(wanderPoint.getY()) < 0.00001f) {
			wanderPoint = new Vector2D(wanderPoint.getX(), 0);
		}
		
		Vector2D wanderTarget = circlePosition.add(wanderPoint); // relative to the entity's position
		
		Debug.getInstance().setData("wanderCircle", circlePosition.add(position));
		Debug.getInstance().setData("wanderTarget", wanderTarget.add(position));
		
		Vector2D wanderAccel = wanderTarget.normalize();
		wanderAccel = wanderAccel.mult(wanderStrength);
		
		return wanderAccel;
	}
}
