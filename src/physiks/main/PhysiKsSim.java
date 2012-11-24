package physiks.main;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import physiks.engine.PhysicsEngine;
import physiks.engine.misc.SpatialData;
import physiks.entities.PolyBody;
import physiks.entities.RigidBody;
import physiks.geometry.Vector2D;
import physiks.main.misc.PhysSimHelper;
import physiks.visual.RenderEngine;

public class PhysiKsSim extends BasicGame {
	private static final String TITLE = "PhysiKs";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	int spawnCooldown;
	
	private PhysicsEngine physEngine;
	private RenderEngine renderEngine;
	public static List<RigidBody> entities;
	private List<SpatialData> initialSpatialData;
	
	public PhysiKsSim(String title) {
		super(title);
	}

	public void init(GameContainer gc) throws SlickException {
		entities = new ArrayList<RigidBody>();
		
		entities.add(PhysSimHelper.createBox(320, HEIGHT - 280, 20, 20, 1));
		entities.get(0).setVelocity(new Vector2D(100, 0));
		entities.add(PhysSimHelper.createBox(400, HEIGHT - 280, 20, 20, 1));

		PhysSimHelper.createObstacles(entities);
		
		// Record the initial spatial data for use when resetting
		initialSpatialData = new ArrayList<SpatialData>();
		for (RigidBody entity : entities) {
			initialSpatialData.add(new SpatialData(entity));
		}
		
		// Initialize engines
		physEngine = new PhysicsEngine(entities);
		renderEngine = new RenderEngine(entities);
		
		spawnCooldown = 0;
	}

	public void update(GameContainer gameContainer, int delta) throws SlickException {
		physEngine.update(delta);

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			System.exit(0);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			PhysSimHelper.reset(entities, initialSpatialData);
		}
		
		spawnCooldown += delta;
		if (Mouse.isButtonDown(0)) {
			spawn(Mouse.getX(), Mouse.getY());
		}
	}

	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		graphics.setAntiAlias(true);
		renderEngine.update(graphics);
	}
	
	private void spawn(int x, int y) {
		if (spawnCooldown < 300) {
			return;
		}
		spawnCooldown = 0;
		
		entities.addAll(PhysSimHelper.spawnRandom(x, y));
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
