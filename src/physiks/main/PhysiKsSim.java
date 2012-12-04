package physiks.main;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import physiks.engine.PhysicsEngine;
import physiks.visual.RenderEngine;
import physiks.entities.PolyBody;
import physiks.entities.RigidBody;
import physiks.forces.Gravity;
import physiks.geometry.Vector2D;
import physiks.main.misc.PhysSimHelper;




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
		
//		PolyBody p1;
//		PolyBody p2;
//		
//		p1 = createBox(100, HEIGHT - 100 - 20, 20, 20, 100);
//		p1.setVelocity(new Vector2D(100, 0));
//		entities.add(p1);
//		
//		p2 = createBox(300, HEIGHT - 100 - 20, 20, 20, 100);
//		entities.add(p2);
		
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
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Display.destroy();
			AL.destroy();
			System.exit(0);
		}
		
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
		
		entities.addAll(PhysSimHelper.spawnRandom(x, y));
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
