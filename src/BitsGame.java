import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import bits.entity.Bit;
import bits.geometry.Vector2D;
import bits.util.Debug;


public class BitsGame extends BasicGame {
	public static final String TITLE = "Bits Byte!";
	public static int WIDTH = 800;
	public static int HEIGHT = 600;
	
	Bit bit;
	
	public BitsGame(String title) {
		super(title);
	}

	public void init(GameContainer gc) throws SlickException {
		bit = new Bit(300, 300, 30, 30);
	}
	
	private void renderWanderTarget(Graphics g) {
		Vector2D wanderTarget = (Vector2D)Debug.getInstance().getData("wanderTarget");
		if (wanderTarget != null) {
			g.draw(new Circle(wanderTarget.getX(), wanderTarget.getY(), 3));
		}
	}
	
	private void renderWanderCircle(Graphics g) {
		Vector2D WanderCircle = (Vector2D)Debug.getInstance().getData("wanderCircle");
		float wanderRadius = (float)Debug.getInstance().getData("wanderRadius");
		if (WanderCircle != null) {
			g.draw(new Circle(WanderCircle.getX(), WanderCircle.getY(), wanderRadius));
		}
	}
	
	private void renderBit(Bit bit, Graphics g) {
		Polygon p = new Polygon();
		p.addPoint(bit.getX() + bit.getWidth() / 2, bit.getY());
		p.addPoint(bit.getX(), bit.getY() + bit.getHeight());
		p.addPoint(bit.getX() + bit.getWidth(), bit.getY() + bit.getHeight());
		p.setClosed(true);
		
		Shape sprite = p;
		Vector2D heading = bit.getHeading();
		
		float slope = heading.getY() / heading.getX();
		float rotation = (float)(Math.atan(slope));

		if (rotation > 0) {
			rotation += Math.PI;
		}
		
		if (heading.getY() > 0) {
			rotation += Math.PI;
		}
		
		rotation += (Math.PI / 2);
		
		Vector2D center = bit.getPosition();
		Transform rotationTransform = Transform.createRotateTransform(rotation, center.getX(), center.getY());
		sprite = sprite.transform(rotationTransform);
		
		g.draw(sprite);
		
		renderWanderTarget(g);
		renderWanderCircle(g);
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setAntiAlias(true);
		renderBit(bit, g);
	}

	public void update(GameContainer gc, int delta) throws SlickException {
		bit.update(delta);
		
		bit.setX(bit.getX() % WIDTH);
		bit.setY(bit.getY() % HEIGHT);
		
		if (bit.getX() + bit.getWidth() < 0) {
			bit.setX(WIDTH);
		}
		if (bit.getY() + bit.getHeight() < 0) {
			bit.setY(HEIGHT);
		}
	}

	public static void main(String[] args) {
		BitsGame game = new BitsGame(BitsGame.TITLE);
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
