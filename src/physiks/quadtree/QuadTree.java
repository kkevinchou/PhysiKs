package physiks.quadtree;

import java.util.ArrayList;
import java.util.List;

import physiks.entities.RigidBody;
import physiks.geometry.Rectangle;

/*
 * Labels for quad tree children:
 * [1][0]
 * [2][3]
 */

public class QuadTree {
	private int maxChildren = 4;
	private int maxDepth = 4;
	private int depth;
	private Rectangle dimension;
	private QuadTree[] nodes;
	private List<RigidBody> elements;

	public QuadTree(int x, int y, int width, int height) {
		this(x, y, width, height, 0);
	}
	
	public QuadTree(int x, int y, int width, int height, int depth) {
		elements = new ArrayList<RigidBody>();
		dimension = new Rectangle(x, y, width, height);
		nodes = null;
		this.depth = depth;
	}
	
	public Rectangle getDimension() {
		return dimension;
	}
	
	public QuadTree[] getNodes() {
		return nodes;
	}
	
	public void add(RigidBody body) {
		if (nodes == null) {
			elements.add(body);
		} else {
			for (int i = 0; i < 4; i++) {
				if (nodes[i].intersects(body)) {
					nodes[i].add(body);
				}
			}
		}
		
		if (elements.size() > maxChildren && depth < maxDepth) {
			split();
		}
	}
	
	public boolean intersects(RigidBody body) {
		Rectangle boundingBox = body.getAABoundingBox();
		
		if (boundingBox.x > (dimension.x + dimension.width - 1)) {
			return false;
		}
		
		if ((boundingBox.x + boundingBox.width - 1) < dimension.x) {
			return false;
		}
		
		if (boundingBox.y > (dimension.y + dimension.height - 1)) {
			return false;
		}
		
		if ((boundingBox.y + boundingBox.height - 1) < dimension.y) {
			return false;
		}
		
		return true;
	}
	
	public List<RigidBody> getIntersectionCandidates(RigidBody body) {
		List<RigidBody> result = new ArrayList<RigidBody>();
		
		if (nodes == null) {
			return elements;
		} else {
			for (int i = 0; i < 4; i++) {
				if (nodes[i].intersects(body)) {
					result.addAll(nodes[i].getIntersectionCandidates(body));
				}
			}
		}
		
		return result;
	}
	
	public void split() {
		nodes = new QuadTree[4];
		
		int childWidth = (int)(dimension.width / 2);
		int childHeight = (int)(dimension.height / 2);
		int childDepth = depth + 1;
		
		nodes[1] = new QuadTree(dimension.x, dimension.y, childWidth, childHeight, childDepth);
		nodes[0] = new QuadTree(dimension.x + childWidth, dimension.y, dimension.width - childWidth, childHeight, childDepth);
		
		nodes[2] = new QuadTree(dimension.x, dimension.y + childHeight, childWidth, dimension.height - childHeight, childDepth);
		nodes[3] = new QuadTree(dimension.x + childWidth, dimension.y + childHeight, dimension.width - childWidth, dimension.height - childHeight, childDepth);
		
		for (RigidBody element : elements) {
			for (int i = 0; i < 4; i++) {
				if (nodes[i].intersects(element)) {
					nodes[i].add(element);
				}
			}
		}
		
		elements.clear();
	}
	
	public void clear() {
		elements.clear();
		
		if (nodes != null) {
			for (int i = 0; i < 4; i++) {
				nodes[i].clear();
			}
		}
		nodes = null;
	}
}
