package bits.client;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import kgeometry.V2D;

import managers.EntityManager;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import entities.MovingEntity;

public class Game {
	private EntityManager entityManager;
	private MovingEntity guy;
	private final float FPS = 60;
	private final float SECONDS_TO_MILLISEC_RATE = 1000;
	private final int screenWidth = 800;
	private final int screenHeight = 600;
	
	public void start() {
		try {
			init();
			gameLoop();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() throws LWJGLException {
		Display.setDisplayMode(new DisplayMode(800,600));
        Display.setTitle("LUMBER DUDES!");
        Display.create();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, screenWidth, screenHeight, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
		
		entityManager = EntityManager.getInstance();
		guy = new MovingEntity(0, 0, 25, 25);
		entityManager.add(guy);
	}
	
	public void gameLoop() throws InterruptedException {
		float milliSecPerFrame = 1 / FPS * SECONDS_TO_MILLISEC_RATE;
		long startTick;
		long elapsedTick;
		float elapsedTimeInSeconds = (float)milliSecPerFrame / 1000;
		
		while (!Display.isCloseRequested()) {
			startTick = System.currentTimeMillis();
			
			handleInput();
			update(elapsedTimeInSeconds);
			render();

			elapsedTick = System.currentTimeMillis() - startTick;
			if (elapsedTick < milliSecPerFrame) {
				Thread.sleep(Math.round(milliSecPerFrame) - elapsedTick);
			}
		}
		
		Display.destroy();
	}
	
	private void update(float dt) {
		guy.update(dt);
	}
	
	private void handleInput() {
		if (Mouse.isButtonDown(1)) {
			int x = Mouse.getX();
			int y = Math.abs(Mouse.getY() - screenHeight);
			
			V2D target = new V2D(x - guy.getWidth()/2, y - guy.getHeight()/2);
			guy.setTarget(target);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			guy.setTarget(null);
		}
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		
		GL11.glColor3f(0.8f, 0.8f, 0.8f);
		glBegin(GL_QUADS);
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f(screenWidth, 0);
			GL11.glVertex2f(screenWidth, screenHeight);
			GL11.glVertex2f(0, screenHeight);
		glEnd();
	
		GL11.glColor3f(0f, 1f, 0f);
		glBegin(GL_QUADS);
			GL11.glVertex2f(guy.getX(), guy.getY());
			GL11.glVertex2f(guy.getX() + guy.getWidth(), guy.getY());
			GL11.glVertex2f(guy.getX() + guy.getWidth(), guy.getY() + guy.getHeight());
			GL11.glVertex2f(guy.getX(), guy.getY() + guy.getHeight());
		glEnd();
		
		Display.update();
	}
}
