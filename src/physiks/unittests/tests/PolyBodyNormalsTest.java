package physiks.unittests.tests;


import java.util.ArrayList;
import java.util.List;

import physiks.entities.PolyBody;
import physiks.geometry.Vector2D;

public final class PolyBodyNormalsTest extends Test {
	@Override
	public final void setup() {
		System.out.println("=== PolyBodyNormalsTest ===");
	}

	@Override
	public final void run() {
		List<Vector2D> points = new ArrayList<Vector2D>();
		points.add(new Vector2D(0, 0));
		points.add(new Vector2D(10, 0));
		points.add(new Vector2D(10, 10));
		points.add(new Vector2D(0, 10));
		
		PolyBody body = new PolyBody(0, 0, 100, points);
		List<Vector2D> normals = body.getNormals();
		
		tAssert(normals.size(), 4);
		
		tAssert(normals.get(0), new Vector2D(0, -1));
		tAssert(normals.get(1), new Vector2D(1, 0));
		tAssert(normals.get(2), new Vector2D(0, 1));
		tAssert(normals.get(3), new Vector2D(-1, 0));
	}
}
