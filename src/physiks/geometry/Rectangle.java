package physiks.geometry;

public class Rectangle {	
	public int x;
	public int y;
	public int width;
	public int height;
	public int b;
	public int c;
	
	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public boolean pointIntersects(Vector2D point, Rectangle target) {
		if ((point.getX() >= target.x) && (point.getX() <= target.x + target.width)) {
			if ((point.getY() >= target.y) && (point.getY() <= target.y + target.height)) {
				return true;
			}
		}
		return false;	
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
