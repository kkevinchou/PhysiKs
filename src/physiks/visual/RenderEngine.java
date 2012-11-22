package physiks.visual;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.ShapeRenderer;

import physiks.PhysiKsSim;
import physiks.entities.*;
import physiks.geometry.Vector2D;

public class RenderEngine {
	private Color backgroundColor;
	private Polygon background;
	private List<RigidBody> entities;
	
	public RenderEngine(List<RigidBody> rigidBodies) {
		backgroundColor = new Color(187, 234, 239);
		background = new Polygon();
		
		background.addPoint(0, 0);
		background.addPoint(PhysiKsSim.WIDTH, 0);
		background.addPoint(PhysiKsSim.WIDTH, PhysiKsSim.HEIGHT);
		background.addPoint(0, PhysiKsSim.HEIGHT);
		background.setClosed(true);
		
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
		graphics.setColor(backgroundColor);
		ShapeRenderer.fill(background);

		for (RigidBody body : entities) {
			drawBody(body, graphics);
		}
		graphics.setColor(backgroundColor);
		graphics.draw(background);
	}
}
