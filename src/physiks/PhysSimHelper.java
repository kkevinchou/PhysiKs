package physiks;

import java.util.ArrayList;
import java.util.List;

import physiks.entities.PolyBody;
import physiks.entities.RigidBody;
import physiks.geometry.Vector2D;

public abstract class PhysSimHelper {
	public static PolyBody createDiamond(float x, float y, float width, float height, float mass) {
		List<Vector2D> points = new ArrayList<Vector2D>();
		
		float maxX = width - 1;
		float maxY = height - 1;
		
		points.add(new Vector2D(maxX / 2, 0));
		points.add(new Vector2D(maxX, maxY / 2));
		points.add(new Vector2D(maxX / 2, maxY));
		points.add(new Vector2D(0, maxY / 2));
		
		return new PolyBody(x, y, mass, points);
	}
	
	public static PolyBody createBox(float x, float y, float width, float height, float mass) {
		List<Vector2D> points = new ArrayList<Vector2D>();
		
		float maxX = width - 1;
		float maxY = height - 1;
		
		points.add(new Vector2D(0, 0));
		points.add(new Vector2D(maxX, 0));
		points.add(new Vector2D(maxX, maxY));
		points.add(new Vector2D(0, maxY));
		
		return new PolyBody(x, y, mass, points);
	}
	
	public static  List<PolyBody> generateWalls() {
		List<PolyBody> walls = new ArrayList<PolyBody>();

		PolyBody bottomWall = createBox(0, PhysiKsSim.HEIGHT - 100, PhysiKsSim.WIDTH, PhysiKsSim.HEIGHT, Float.POSITIVE_INFINITY);
		PolyBody leftWall = createBox(-PhysiKsSim.WIDTH, 0, PhysiKsSim.WIDTH, PhysiKsSim.HEIGHT, Float.POSITIVE_INFINITY);
		PolyBody rightWall = createBox(PhysiKsSim.WIDTH, 0, PhysiKsSim.WIDTH, PhysiKsSim.HEIGHT, Float.POSITIVE_INFINITY);
		PolyBody topWall = createBox(0, -PhysiKsSim.HEIGHT, PhysiKsSim.WIDTH, PhysiKsSim.HEIGHT, Float.POSITIVE_INFINITY);
		
		walls.add(bottomWall);
		walls.add(leftWall);
		walls.add(rightWall);
		walls.add(topWall);
		
		return walls;
	}
	
	public static RigidBody spawnBlock(int x, int y) {		
		float mass = 100;
		float width = 20;
		float height = 20;
		
		RigidBody body = createDiamond(x, PhysiKsSim.HEIGHT - y, width, height, mass);
		return body;
	}
	
	public static List<RigidBody> spawnRandom(int x, int y) {		
		int xSpread = 200;
		int ySpread = 200;
		
		float width = 20;
		float height = 20;
		float mass = 100;
		
		RigidBody body;
		
		List<RigidBody> entities = new ArrayList<RigidBody>();
		
		for (int i = 0; i < 20; i++) {
			float xPos = (float)Math.random() * xSpread + x - xSpread / 2;
			float yPos = (float)Math.random() * ySpread + (PhysiKsSim.HEIGHT - y) - ySpread / 2;
			
			if (i % 2 == 3) {
				body = createBox(xPos, yPos, width, height, mass);
			} else {
				body = createDiamond(xPos, yPos, width, height, mass);
			}

			entities.add(body);
		}
		
		return entities;
	}
}
