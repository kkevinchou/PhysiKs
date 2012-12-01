package physiks.quadtree;

import java.util.List;

import physiks.entities.RigidBody;
import physiks.main.PhysiKsSim;

public class Partitioner {
	private static Partitioner instance = new Partitioner();
	private List<RigidBody> bodies;
	private QuadTree quadTree;
	
	private Partitioner() {
		quadTree = new QuadTree(-PhysiKsSim.WIDTH, -PhysiKsSim.HEIGHT, PhysiKsSim.WIDTH*3, PhysiKsSim.HEIGHT*3, 1000);
	}
	
	public static Partitioner getInstance() {
		return instance;
	}
	
	public void reset() {
		quadTree.clear();
		for (RigidBody body : bodies) {
			quadTree.add(body);
		}
	}
	
	public void addAllBodies(List<RigidBody> bodies) {
		for (RigidBody body : bodies) {
			this.bodies.add(body);
		}
	}
	
	public void addBody(RigidBody body) {
		bodies.add(body);
	}
	
	public List<RigidBody> getCollisionCandidates(RigidBody b) {
		return quadTree.getIntersectionCandidates(b);
	}
}
