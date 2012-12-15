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

import physiks.audio.AudioPlayer;
import physiks.engine.PhysicsEngine;
import physiks.engine.misc.SpatialData;
import physiks.entities.RigidBody;
import physiks.geometry.Vector2D;
import physiks.main.misc.PhysSimHelper;
import physiks.visual.RenderEngine;

public class PhysiKsSim extends BasicGame {
	private static final String TITLE = "PhysiKs";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	private int spawnCooldown = 0;
	private int buttonCooldown = 0;
	public static Mode MODE = Mode.Normal;
	
	private PhysicsEngine physEngine;
	private RenderEngine renderEngine;
	public static List<RigidBody> entities;
	
	public static int testId = 999;
	public static boolean testDebug = true;
	
	public enum Mode {
		Normal, Frame, Step
	}
	
	public PhysiKsSim(String title) {
		super(title);
	}

	public void init(GameContainer gc) throws SlickException {
		entities = new ArrayList<RigidBody>();
		
//		RigidBody b1 = PhysSimHelper.createDiamond(0, PhysiKsSim.HEIGHT - 120, 20, 20, 1);
//		RigidBody b2 = PhysSimHelper.createDiamond(100, PhysiKsSim.HEIGHT - 120, 20, 20, 1);
//		b2.setVelocity(new Vector2D(-50, 0));
//		RigidBody b3 = PhysSimHelper.createDiamond(120, PhysiKsSim.HEIGHT - 120, 20, 20, 1);
//		b3.setVelocity(new Vector2D(-50, 0));
//		
//		entities.add(b1);
//		entities.add(b2);
//		entities.add(b3);

		PhysSimHelper.createObstacles(entities);
		
		// Initialize engines
		physEngine = new PhysicsEngine(entities);
		renderEngine = new RenderEngine(entities);
		
		AudioPlayer.getInstance().setSoundsEnabled(false);
	}

	public void update(GameContainer gameContainer, int delta) throws SlickException {
		buttonCooldown += 16;

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Display.destroy();
			AL.destroy();
			System.exit(0);
		}
		
		if (buttonCooldown >= 300) {
			if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
				if (MODE == Mode.Normal) {
					MODE = Mode.Frame;
				} else if (MODE == Mode.Frame) {
					MODE = Mode.Normal;
				}
			}
			buttonCooldown = 0;
		}
		
		spawnCooldown += delta;
		if (Mouse.isButtonDown(0)) {
			spawn(Mouse.getX(), Mouse.getY(), true);
		} else if (Mouse.isButtonDown(1)){
			spawn(Mouse.getX(), Mouse.getY(), false);
		}
		
		switch (MODE) {
			case Normal:
				physEngine.update(delta);
				break;
			case Frame:
				frameFunction();
				break;
			case Step:
				if (buttonCooldown < 300) break;
				frameFunction();
				buttonCooldown = 0;
			default:
				break;
		}
	}
	
	private void frameFunction() {
		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			try	{
				physEngine.update(16);
			} catch(Exception e) {
				physEngine.stepBack();
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			physEngine.stepBack();
		}
	}

	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		graphics.setAntiAlias(true);
		renderEngine.update(graphics);
	}
	
	private void spawn(int x, int y, boolean LeftButton) {
		if (spawnCooldown < 300) {
			return;
		}
		
		if (LeftButton) {
			entities.addAll(PhysSimHelper.spawnRandom(x, y, 1));
		} else {
			RigidBody b = PhysSimHelper.spawnDiamond(x, y, 1);
			b.setVelocity(Vector2D.LEFT.mult(50));
			b.setId(++testId);
			testDebug = true;
			entities.add(b);
		}

		spawnCooldown = 0;
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
