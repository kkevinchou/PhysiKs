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
	private int stepCooldown = 0;
	public static Mode MODE = Mode.Frame;
	
	private PhysicsEngine physEngine;
	private RenderEngine renderEngine;
	public static List<RigidBody> entities;
	
	public enum Mode {
		Normal, Frame, Step
	}
	
	public PhysiKsSim(String title) {
		super(title);
	}

	public void init(GameContainer gc) throws SlickException {
		entities = new ArrayList<RigidBody>();
		
//		entities.add(PhysSimHelper.createDiamond(100, PhysiKsSim.HEIGHT - 120, 20, 20, 1));
//		entities.get(0).setVelocity(new Vector2D(100, 0));
//		entities.add(PhysSimHelper.createDiamond(400, PhysiKsSim.HEIGHT - 200, 20, 20, 1));
//		entities.get(1).setVelocity(new Vector2D(-200, 0));
//		entities.add(PhysSimHelper.createDiamond(390, PhysiKsSim.HEIGHT - 140, 20, 20, 1));

		PhysSimHelper.createObstacles(entities);
		
		// Initialize engines
		physEngine = new PhysicsEngine(entities);
		renderEngine = new RenderEngine(entities);
		
		AudioPlayer.getInstance().setSoundsEnabled(false);
	}

	public void update(GameContainer gameContainer, int delta) throws SlickException {
		switch (MODE) {
			case Normal:
				physEngine.update(delta);
				break;
			case Frame:
				if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
					try	{
						physEngine.update(16);
					} catch(Exception e) {
						physEngine.stepBack();
					}
				} else if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
					physEngine.stepBack();
				}
				break;
			case Step:
				stepCooldown += 16;
				
				if (stepCooldown < 300) break;
				
				if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
					try	{
						physEngine.update(16);
					} catch(Exception e) {
						physEngine.stepBack();
					}
				} else if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
					physEngine.stepBack();
				}
				
				stepCooldown = 0;
			default:
				break;
		}

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

	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		graphics.setAntiAlias(true);
		renderEngine.update(graphics);
	}
	
	private void spawn(int x, int y) {
		if (spawnCooldown < 300) {
			return;
		}
		System.out.println("Mouse: " + Mouse.getX() + ", " + Mouse.getY());
		
//		entities.add(PhysSimHelper.spawnDiamond(x, y, 10));
		entities.addAll(PhysSimHelper.spawnRandom(x, y, 10));

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
