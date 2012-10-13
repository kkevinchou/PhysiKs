package physiks;
import java.util.ArrayList;
import java.util.List;

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
	
	private PhysicsEngine physEngine;
	private RenderEngine renderEngine;
	private List<RigidBody> entities;
	
	public PhysiKsSim(String title) {
		super(title);
	}

	public void init(GameContainer gc) throws SlickException {
		entities = new ArrayList<RigidBody>();
		
		List<Vector2D> points = new ArrayList<Vector2D>();
		points.add(new Vector2D(0, 0));
		points.add(new Vector2D(20, 0));
		points.add(new Vector2D(10, 20));
		
		RigidBody body1 = new PolyBody(100, 300, 25, points);
		body1.setVelocity(new Vector2D(100, 0));
		entities.add(body1);

		physEngine = new PhysicsEngine(entities);
		renderEngine = new RenderEngine(entities);
	}

	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		graphics.setAntiAlias(true);
		renderEngine.update(graphics);
	}

	public void update(GameContainer gameContainer, int delta) throws SlickException {
		physEngine.update(delta);
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
