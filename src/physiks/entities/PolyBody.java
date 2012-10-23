package physiks.entities;

import java.util.ArrayList;
import java.util.List;

import physiks.geometry.Rectangle;
import physiks.geometry.Vector2D;

public class PolyBody extends RigidBody {
	List<Vector2D> normals;
	List<Vector2D> points;
	Vector2D center;

	public PolyBody(float x, float y, float mass, List<Vector2D> points) {
		super(x, y, mass);
		normals = new ArrayList<Vector2D>();
		this.points = new ArrayList<Vector2D>();
		center = Vector2D.ZERO_VECTOR;
		
		for (Vector2D point : points) {
			center = center.add(point);
			this.points.add(point.copy());
		}
		center = center.div(points.size());
		
		for (int i = 0, size = points.size(); i < size; i++) {
			Vector2D a = points.get(i);
			Vector2D b = points.get((i + 1) % size);
			
			Vector2D normal = b.sub(a).perpendicular().normalize();
			if (normal.normalizedProjection(a.sub(center)) > 0) {
				// Normal faces outward from center, add it to our normals list
				normals.add(normal);
			} else {
				// Normal faces inward, flip it so that it faces out from center
				normals.add(normal.mult(-1));
			}
		}
	}
	
	public Rectangle getAABoundingBox() {
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		
		
		for (Vector2D point : getPoints()) {
			float x = point.getX();
			float y = point.getY();
			
			if (x > maxX) {
				maxX = (int)x;
			}
			
			if (x < minX) {
				minX = (int)x;
			}
			
			if (y > maxY) {
				maxY = (int)y;
			}
			
			if (y < minY) {
				minY = (int)y;
			}
		}
		
		return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
	}
	
	public List<Vector2D> getPoints() {
		List<Vector2D> worldPoints = new ArrayList<Vector2D>();
		Vector2D position = getPosition();
		
		for (Vector2D point : points) {
			point = new Vector2D(position.getX() + point.getX(), position.getY() + point.getY());
			worldPoints.add(point);
		}
		return worldPoints;
	}

	public final List<Vector2D> getNormals() {
		return normals;
	}
}
