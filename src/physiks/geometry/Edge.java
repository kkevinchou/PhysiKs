package physiks.geometry;

import java.util.ArrayList;
import java.util.List;

public class Edge {
	private List<Vector2D> points;
	
	public Edge(Vector2D a, Vector2D b) {
		points = new ArrayList<Vector2D>();
		points.add(a);
		points.add(b);
	}
	
	public Vector2D getA() {
		return points.get(0);
	}
	
	public Vector2D getB() {
		return points.get(1);
	}
}
