package entities;

import java.util.ArrayList;
import java.util.List;
import geometry.Vector2D;

public class PolyBody extends RigidBody {
	List<Vector2D> normals;
	List<Vector2D> points;
	Vector2D center;

	public PolyBody(float x, float y, float mass, List<Vector2D> points) {
		super(x, y, mass);
		normals = new ArrayList<Vector2D>();
		this.points = new ArrayList<Vector2D>();
		center = Vector2D.zeroVector();
		
		for (Vector2D point : points) {
			center = center.add(point);
			this.points.add(point.copy());
		}
		center = center.div(points.size());
		
		for (int i = 0, size = points.size(); i < size; i++) {
			Vector2D a = points.get(i);
			Vector2D b = points.get(i % size);
			
			Vector2D normal = b.sub(a).perpendicular();
			if (center.add(normal).normalizedProjection(a.sub(center)) > 0) {
				// Normal faces outward from center, add it to our normals list
				normals.add(normal);
			} else {
				// Normal faces inward, flip it so that it faces out from center
				normals.add(normal.mult(-1));
			}
		}
	}
	
	public List<Vector2D> getPoints() {
		return points;
	}

}
