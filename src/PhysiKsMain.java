import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import engine.PhysicsEngine;
import engine.RenderEngine;
import entities.Entity;


public class PhysiKsMain extends BasicGame {
	private static final String TITLE = "PhysiKs";
	public static int WIDTH = 800;
	public static int HEIGHT = 600;
	
	private PhysicsEngine physEngine;
	private RenderEngine renderEngine;
	private List<Entity> entities;
	
	public PhysiKsMain(String title) {
		super(title);
	}

	public void init(GameContainer gc) throws SlickException {
		entities = new ArrayList<Entity>();
		entities.add(new Entity(100, 100, 30));

		physEngine = new PhysicsEngine(entities);
		renderEngine = new RenderEngine(entities);
	}

	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		graphics.setAntiAlias(true);
		renderEngine.update(graphics);
	}

	public void update(GameContainer gameContainer, int delta) throws SlickException {
	}

	public static void main(String[] args) {
		PhysiKsMain game = new PhysiKsMain(PhysiKsMain.TITLE);
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
