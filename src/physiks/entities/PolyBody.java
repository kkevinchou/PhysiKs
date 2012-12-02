package physiks.entities;

import java.util.ArrayList;
import java.util.List;

import physiks.geometry.Rectangle;
import physiks.geometry.Vector2D;

public class PolyBody extends RigidBody {
	List<Vector2D> normals;
	List<Vector2D> points;
	
	public PolyBody(float x, float y, float width, float height) {
		super(x, y, 1);
		points = new ArrayList<Vector2D>();
		
		points.add(new Vector2D(0, 0));
		points.add(new Vector2D(width - 1, 0));
		points.add(new Vector2D(width - 1, height - 1));
		points.add(new Vector2D(0, height - 1));
	}

	public PolyBody(float x, float y, float mass, List<Vector2D> points) {
		super(x, y, mass);
		
		normals = new ArrayList<Vector2D>();
		this.points = new ArrayList<Vector2D>();
		Vector2D center = Vector2D.ZERO;
		
		for (Vector2D point : points) {
			center.add(point);
			this.points.add(point);
		}
		setCenter(center.div(points.size()));
		
		for (int i = 0, size = points.size(); i < size; i++) {
			Vector2D a = points.get(i);
			Vector2D b = points.get((i + 1) % size);
			
			Vector2D normal = b.sub(a).perpendicular().normalize();
			if (normal.pointsInSameDirection(a.sub(getCenter()))) {
				// Normal faces outward from center, add it to our normals list
				normals.add(normal);
			} else {
				// Normal faces inward, flip it so that it faces out from center
				normals.add(normal.mult(-1));
			}
		}
	}
	
	public Rectangle getAABoundingBox() {
		float maxX = Integer.MIN_VALUE;
		float maxY = Integer.MIN_VALUE;
		float minX = Integer.MAX_VALUE;
		float minY = Integer.MAX_VALUE;
		
		
		for (Vector2D point : getPoints()) {
			float x = point.getX();
			float y = point.getY();
			
			if (x > maxX) {
				maxX = x;
			} else if (x < minX) {
				minX = x;
			}
			
			if (y > maxY) {
				maxY = y;
			} else if (y < minY) {
				minY = y;
			}
		}
		
		float boundingBoxWidth = maxX - minX + 1;
		float boundingBoxHeight = maxY - minY + 1;
		
		return new Rectangle(minX, minY, boundingBoxWidth, boundingBoxHeight);
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
