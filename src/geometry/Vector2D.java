package geometry;

public final class Vector2D {
	public float x;
	public float y;
	
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
	
	public final Vector2D add(final Vector2D vector) {
		return new Vector2D(this.x + vector.getX(), this.y + vector.getY());
	}
	
	public final Vector2D sub(final Vector2D vector) {
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
		if (magnitude == 0) return new Vector2D(this.x, this.y);
		return new Vector2D((float)(x / magnitude), (float)(y / magnitude));
	}
	
	public float magnitude() {
		return (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public float magnitudeSq() {
		return (float)(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public String toString() {
		return "[Vector2D X: " + this.x + " Y: " + this.y + "]";
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
