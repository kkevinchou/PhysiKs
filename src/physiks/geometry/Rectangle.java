package physiks.geometry;

public class Rectangle {	
	public float x;
	public float y;
	public float width;
	public float height;
	
	public Rectangle(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Rectangle)) {
			return false;
		}
		Rectangle r = (Rectangle)o;
		return (x == r.x && y == r.y && width == r.width && height == r.height);
	}
	
	public String toString() {
		return "[Rectangle X: " + this.x + " Y: " + this.y + " WIDTH: " + this.width + " HEIGHT: " + this.height + "]";
	}
}
