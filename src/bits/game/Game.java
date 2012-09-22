package bits.game;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import bits.entity.*;

public class Game {
	public static final int FPS = 60;
	public static final int width = 800;
	public static final int height = 600;
	
	private void runGame() {
		Director director = new Director();
		
		while (!Display.isCloseRequested()) {
			clearScreen();
			director.update(0);
			renderScreen();
		}

		Display.destroy();
	}
	
	private void initializeScreen() {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		initializeOpenGL();
	}
	
	private void initializeOpenGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	private void clearScreen() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glColor3f(1f, 1f, 1f);
		GL11.glPushMatrix();
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(0, 0);
				GL11.glVertex2f(width, 0);
				GL11.glVertex2f(width, height);
				GL11.glVertex2f(0, height);
			GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	private void renderScreen() {
		Display.update();
		Display.sync(FPS);
	}
	
	public void start() {
		initializeScreen();
		runGame();
	}
}
