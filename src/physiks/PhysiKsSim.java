package physiks;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


import engine.PhysicsEngine;
import engine.RenderEngine;
import entities.PolyBody;
import entities.RigidBody;
import forces.Gravity;
import geometry.Vector2D;


public class PhysiKsSim extends BasicGame {
	private static final String TITLE = "PhysiKs";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	int spawnCooldown;
	
	private PhysicsEngine physEngine;
	private RenderEngine renderEngine;
	private List<RigidBody> entities;
	
	public PhysiKsSim(String title) {
		super(title);
	}

	public void init(GameContainer gc) throws SlickException {
		entities = new ArrayList<RigidBody>();
		
		PolyBody p;
		
		p = createDiamond(100, HEIGHT - 100 - 20, 20, 20, 100);
		p.setVelocity(new Vector2D(100, 0));
		entities.add(p);
		
		p = createDiamond(500, HEIGHT - 100 - 20, 20, 20, 100);
		p.setVelocity(new Vector2D(-100, 0));
		entities.add(p);
		
		List<PolyBody> walls = generateWalls();
		entities.addAll(walls);

		physEngine = new PhysicsEngine(entities);
		renderEngine = new RenderEngine(entities);
		
		spawnCooldown = 0;
	}
	
	private List<PolyBody> generateWalls() {
		List<PolyBody> walls = new ArrayList<PolyBody>();
		
		PolyBody leftWall = createBox(-WIDTH, 0, WIDTH, HEIGHT, Float.POSITIVE_INFINITY);
		PolyBody rightWall = createBox(WIDTH, 0, WIDTH, HEIGHT, Float.POSITIVE_INFINITY);
		PolyBody topWall = createBox(0, -HEIGHT, WIDTH, HEIGHT, Float.POSITIVE_INFINITY);
		PolyBody bottomWall = createBox(0, HEIGHT - 100, WIDTH, HEIGHT, Float.POSITIVE_INFINITY);
		
		walls.add(leftWall);
		walls.add(rightWall);
		walls.add(topWall);
		walls.add(bottomWall);
		
		return walls;
	}

	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		graphics.setAntiAlias(true);
		renderEngine.update(graphics);
	}

	public void update(GameContainer gameContainer, int delta) throws SlickException {
		physEngine.update(delta);
		
		spawnCooldown += delta;
		if (Mouse.isButtonDown(0)) {
			spawn(Mouse.getX(), Mouse.getY());
		}
	}
	
	private void spawn(int x, int y) {
		if (spawnCooldown < 300) {
			return;
		}
		spawnCooldown = 0;
		
		spawnRandom(x, y);
	}
	
	private void spawnBlock(int x, int y) {		
		float mass = 100;
		float width = 20;
		float height = 20;
		
		RigidBody body = createDiamond(x, HEIGHT - y, width, height, mass);
		
		entities.add(body);
	}
	
	private void spawnRandom(int x, int y) {		
		int xSpread = 200;
		int ySpread = 200;
		
		float width = 20;
		float height = 20;
		float mass = 100;
		
		RigidBody body;
		
		for (int i = 0; i < 10; i++) {
			float xPos = (float)Math.random() * xSpread + x - xSpread / 2;
			float yPos = (float)Math.random() * ySpread + (HEIGHT - y) - ySpread / 2;
			
			if (i % 2 == 0) {
				body = createDiamond(xPos, yPos, width, height, mass);
			} else {
				body = createBox(xPos, yPos, width, height, mass);
			}

			entities.add(body);
		}
	}
	
	private PolyBody createDiamond(float x, float y, float width, float height, float mass) {
		List<Vector2D> points = new ArrayList<Vector2D>();
		points.add(new Vector2D(width / 2, 0));
		points.add(new Vector2D(width, height / 2));
		points.add(new Vector2D(width / 2, height));
		points.add(new Vector2D(0, height / 2));
		
		return new PolyBody(x, y, mass, points);
	}
	
	private PolyBody createBox(float x, float y, float width, float height, float mass) {
		List<Vector2D> points = new ArrayList<Vector2D>();
		points.add(new Vector2D(0, 0));
		points.add(new Vector2D(width, 0));
		points.add(new Vector2D(width, height));
		points.add(new Vector2D(0, height));
		
		return new PolyBody(x, y, mass, points);
	}

	public static void main(String[] args) {
		PhysiKsSim game = new PhysiKsSim(PhysiKsSim.TITLE);
	     try {
	          AppGameContainer app = new AppGameContainer(game);
	          app.setDisplayMode(WIDTH, HEIGHT, false);
	          app.setTargetFrameRate(60);
	          app.setShowFPS(false);
	          app.start();
	     } catch (SlickException e) {
	          e.printStackTrace();
	     }
	}
}
