package physiks.geometry;

public final class Vector2D {
	private final float x;
	private final float y;
	
	public static final Vector2D ZERO_VECTOR = new Vector2D(0, 0);
	
	public Vector2D() {
		this(0, 0);
	}
	
	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	// Hopefully this doesn't bite me in the ass one day
	public Vector2D(double x, double y) {
		this.x = (float)x;
		this.y = (float)y;
	}
	
	public Vector2D copy() {
		return new Vector2D(x, y);
	}
	
	public Vector2D perpendicular() {
		return new Vector2D(-y, x);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public float magnitude() {
		return (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public float magnitudeSq() {
		return (float)(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public float dot(Vector2D v) {
		return (x * v.getX()) + (y * v.getY());
	}
	
	public float normalizedProjection(Vector2D v) {
		return dot(v.normalize());
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Vector2D)) {
			return false;
		}
		
		Vector2D vector = (Vector2D)o;
		if ((x == vector.getX()) && (y == vector.getY())) {
			return true;
		}
		
		return false;
	}
	
	public String toString() {
		return "[Vector2D X: " + this.x + " Y: " + this.y + "]";
	}
	
	public final Vector2D add(Vector2D vector) {
		return new Vector2D(this.x + vector.getX(), this.y + vector.getY());
	}
	
	public final Vector2D sub(Vector2D vector) {
		return new Vector2D(this.x - vector.getX(), this.y - vector.getY());
	}
	
	public final Vector2D mult(float scalar) {
		return new Vector2D(this.x * scalar, this.y * scalar);
	}
	
	public final Vector2D div(float scalar) {
		return new Vector2D(this.x / scalar, this.y / scalar);
	}
	
	public final Vector2D normalize() {
		float magnitude = magnitude();
		if (magnitude == 0) {
			System.out.println("Error: Attempted to normalize a 0 magnitude vector");
		}
		return new Vector2D((float)(x / magnitude), (float)(y / magnitude));
	}
}
