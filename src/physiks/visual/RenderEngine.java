package physiks.visual;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import physiks.entities.*;
import physiks.geometry.Vector2D;

public class RenderEngine {
	private List<RigidBody> entities;
	
	public RenderEngine(List<RigidBody> rigidBodies) {
		this.entities = rigidBodies;
	}
	
	private void drawBody(RigidBody b, Graphics graphics) {
		PolyBody body = (PolyBody)b;
		Polygon sprite = new Polygon();
		VisualData visualData = b.getVisualComponent();
		
		for (Vector2D point : body.getPoints()) {
			float x = point.getX();
			float y = point.getY();
			sprite.addPoint(x, y);
		}
		sprite.setClosed(true);
		
		graphics.setColor(visualData.getColor());
		graphics.draw(sprite);
	}
	
	public void update(Graphics graphics) {
		for (RigidBody body : entities) {
			drawBody(body, graphics);
		}
	}
}
