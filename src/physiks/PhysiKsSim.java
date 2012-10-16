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
		
		List<Vector2D> points1 = new ArrayList<Vector2D>();
		points1.add(new Vector2D(10, 0));
		points1.add(new Vector2D(20, 10));
		points1.add(new Vector2D(10, 20));
		points1.add(new Vector2D(0, 10));
		
		List<Vector2D> points2 = new ArrayList<Vector2D>();
		points2.add(new Vector2D(0, 0));
		points2.add(new Vector2D(20, 0));
		points2.add(new Vector2D(20, 20));
		points2.add(new Vector2D(0, 20));
		
		RigidBody body1 = new PolyBody(100, 285, 25, points1);
		RigidBody body2 = new PolyBody(300, 300, 25, points2);
		body1.setVelocity(new Vector2D(200, 0));
		
		entities.add(body1);
		entities.add(body2);
		
		List<PolyBody> walls = generateWalls();
		entities.addAll(walls);

		physEngine = new PhysicsEngine(entities);
		renderEngine = new RenderEngine(entities);
	}
	
	private List<PolyBody> generateWalls() {
		List<PolyBody> walls = new ArrayList<PolyBody>();
		
		List<Vector2D> leftWallPoints = new ArrayList<Vector2D>();
		leftWallPoints.add(new Vector2D(0, 0));
		leftWallPoints.add(new Vector2D(WIDTH, 0));
		leftWallPoints.add(new Vector2D(WIDTH, HEIGHT));
		leftWallPoints.add(new Vector2D(0, HEIGHT));
		PolyBody leftWall = new PolyBody(-WIDTH, 0, Float.POSITIVE_INFINITY, leftWallPoints);
		
		List<Vector2D> rightWallPoints = new ArrayList<Vector2D>();
		rightWallPoints.add(new Vector2D(0, 0));
		rightWallPoints.add(new Vector2D(WIDTH, 0));
		rightWallPoints.add(new Vector2D(WIDTH, HEIGHT));
		rightWallPoints.add(new Vector2D(0, HEIGHT));
		PolyBody rightWall = new PolyBody(WIDTH, 0, Float.POSITIVE_INFINITY, rightWallPoints);
		
		List<Vector2D> topWallPoints = new ArrayList<Vector2D>();
		topWallPoints.add(new Vector2D(0, 0));
		topWallPoints.add(new Vector2D(WIDTH, 0));
		topWallPoints.add(new Vector2D(WIDTH, HEIGHT));
		topWallPoints.add(new Vector2D(0, HEIGHT));
		PolyBody topWall = new PolyBody(0, -HEIGHT, Float.POSITIVE_INFINITY, topWallPoints);
		
		List<Vector2D> bottomWallPoints = new ArrayList<Vector2D>();
		bottomWallPoints.add(new Vector2D(0, 0));
		bottomWallPoints.add(new Vector2D(WIDTH, 0));
		bottomWallPoints.add(new Vector2D(WIDTH, HEIGHT));
		bottomWallPoints.add(new Vector2D(0, HEIGHT));
		PolyBody bottomWall = new PolyBody(0, HEIGHT, Float.POSITIVE_INFINITY, bottomWallPoints);
		
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
