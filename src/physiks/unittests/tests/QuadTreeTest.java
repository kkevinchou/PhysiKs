package physiks.unittests.tests;

import java.util.ArrayList;
import java.util.List;

import physiks.entities.PolyBody;
import physiks.entities.RigidBody;
import physiks.geometry.Vector2D;
import physiks.quadtree.QuadTree;

public class QuadTreeTest extends Test {

	@Override
	public void setup() {
		System.out.println("=== QuadTreeTest ===");
	}

	@Override
	public void run() {
		fractionalPositionTest();
		basicIntersectionTest();
		bodyIntersectionTest();
	}
	
	private void basicIntersectionTest() {
		QuadTree q = new QuadTree(0, 0, 5, 5);
		
		List<Vector2D> points = new ArrayList<Vector2D>();
		points.add(new Vector2D(0, 0));
		points.add(new Vector2D(1, 0));
		points.add(new Vector2D(1, 1));
		points.add(new Vector2D(0, 1));
		
		PolyBody body = new PolyBody(0, 0, 1, points);
		
		body.setPosition(new Vector2D(0, 0));
		tAssert(q.intersects(body), true);
		
		body.setPosition(new Vector2D(-1, 0));
		tAssert(q.intersects(body), true);
		
		body.setPosition(new Vector2D(4, 0));
		tAssert(q.intersects(body), true);
		
		body.setPosition(new Vector2D(5, 0));
		tAssert(q.intersects(body), false);
		
		body.setPosition(new Vector2D(0, -1));
		tAssert(q.intersects(body), true);
		
		body.setPosition(new Vector2D(0, -2));
		tAssert(q.intersects(body), false);
		
		body.setPosition(new Vector2D(0, -1));
		tAssert(q.intersects(body), true);
		
		body.setPosition(new Vector2D(0, 3));
		tAssert(q.intersects(body), true);
		
		body.setPosition(new Vector2D(0, 5));
		tAssert(q.intersects(body), false);
	}
	
	private void bodyIntersectionTest() {
		QuadTree q = new QuadTree(0, 0, 5, 5);
		
		List<Vector2D> points = new ArrayList<Vector2D>();
		points.add(new Vector2D(0, 0));
		points.add(new Vector2D(1, 0));
		points.add(new Vector2D(1, 1));
		points.add(new Vector2D(0, 1));
		
		PolyBody body1 = new PolyBody(2, 0, 1, points); // node 0
		PolyBody body2 = new PolyBody(0, 0, 1, points); // node 1
		PolyBody body3 = new PolyBody(0, 2, 1, points); // node 2
		PolyBody body4 = new PolyBody(3, 3, 1, points); // node 3
		PolyBody body5 = new PolyBody(3, 3, 1, points); // node 3
		
		q.add(body1);
		q.add(body2);
		q.add(body3);
		q.add(body4);
		q.add(body5);
		
		PolyBody testBody = new PolyBody(0, 0, 1, points); // node 0
		List<RigidBody> candidates;
		
		testBody.setPosition(new Vector2D(3, 0));
		candidates = q.getIntersectionCandidates(testBody);
		tAssert(candidates.size(), 1);
		
		testBody.setPosition(new Vector2D(0, 0));
		candidates = q.getIntersectionCandidates(testBody);
		tAssert(candidates.size(), 3);
		
		testBody.setPosition(new Vector2D(0, 2));
		candidates = q.getIntersectionCandidates(testBody);
		tAssert(candidates.size(), 3);
		
		testBody.setPosition(new Vector2D(1, 0));
		candidates = q.getIntersectionCandidates(testBody);
		tAssert(candidates.size(), 3);
		
		testBody.setPosition(new Vector2D(1, 2));
		candidates = q.getIntersectionCandidates(testBody);
		tAssert(candidates.size(), 5);
		
		testBody.setPosition(new Vector2D(1, 1));
		candidates = q.getIntersectionCandidates(testBody);
		tAssert(candidates.size(), 5);
	}
	
	private void fractionalPositionTest() {
		QuadTree q = new QuadTree(0, 0, 2, 2);
		
		q.add(new PolyBody(1, 0, 1, 1));
		q.add(new PolyBody(1, 1, 1, 1));
		q.add(new PolyBody(1, 1, 1, 1));
		q.add(new PolyBody(1, 1, 1, 1));
		q.add(new PolyBody(1, 1, 1, 1));
		
		PolyBody testBody = new PolyBody(0.5f, 0, 1, 1);
		
		List<RigidBody> candidates = q.getIntersectionCandidates(testBody);
		tAssert(candidates.size(), 1);
	}
	
	// TODO: Test adding bodies outside of quadtree range
}
