import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import bits.entity.Bit;
import bits.geometry.Vector2D;
import bits.util.Debug;


public class BitsGame extends BasicGame {
	public static final String TITLE = "Bits Byte!";
	public static int WIDTH = 800;
	public static int HEIGHT = 600;
	
	Bit b;
	float rotation;
	
	public BitsGame(String title) {
		super(title);
	}

	public void init(GameContainer gc) throws SlickException {
		b = new Bit(300, 300, 30, 30);
		rotation = 0;
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		Vector2D heading = b.getHeading();
		// may need to multiply by -1 to account for inverted axis
		float baseRotation = (float) (Math.PI / 2);
		float rotationAdjustment = (float)(Math.PI - Math.atan(heading.getY() / heading.getX()));
		
		float rotation = baseRotation;
		if (heading.getX() > 0) {
			rotation = baseRotation - rotationAdjustment;
		} else {
			rotation = baseRotation + rotationAdjustment;
		}
		
		Vector2D center = b.getPosition();
		Transform rotationTransform = Transform.createRotateTransform((float)Math.toRadians(-rotation++), center.getX(), center.getY());
		Shape sprite = new Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
		sprite = sprite.transform(rotationTransform);
		g.draw(sprite);
		
		Vector2D wanderTarget = (Vector2D)Debug.getInstance().getData("wanderTarget");
		if (wanderTarget != null) {
			g.draw(new Circle(wanderTarget.getX(), wanderTarget.getY(), 3));
		}
		
		Vector2D circle = (Vector2D)Debug.getInstance().getData("wanderCircle");
		float wanderRadius = (float)Debug.getInstance().getData("wanderRadius");
		if (circle != null && wanderRadius > 0) {
			g.draw(new Circle(circle.getX(), circle.getY(), wanderRadius));
		}
	}

	public void update(GameContainer gc, int delta) throws SlickException {
		b.update(delta);
		
		if (b.getX() < 0) {
			b.setX(WIDTH);
		}
		if (b.getX() > WIDTH) {
			b.setX(0);
		}
		if (b.getY() < 0) {
			b.setY(HEIGHT);
		}
		if (b.getY() > HEIGHT){
			b.setY(0);
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
