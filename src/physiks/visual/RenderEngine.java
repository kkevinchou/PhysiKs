package physiks.visual;

import java.util.List;

import org.newdawn.slick.Color;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.ShapeRenderer;

import physiks.entities.*;
import physiks.geometry.Vector2D;
import physiks.main.PhysiKsSim;

public class RenderEngine {
	private Color backgroundColor;
	private Polygon background;
	private List<RigidBody> entities;
	
	public RenderEngine(List<RigidBody> rigidBodies) {
		this.entities = rigidBodies;
		initBackground();
	}
	
	private void initBackground() {
		backgroundColor = new Color(0, 0, 0);
		background = new Polygon();
		
		background.addPoint(0, 0);
		background.addPoint(PhysiKsSim.WIDTH, 0);
		background.addPoint(PhysiKsSim.WIDTH, PhysiKsSim.HEIGHT);
		background.addPoint(0, PhysiKsSim.HEIGHT);
		background.setClosed(true);
	}

	public void update(Graphics graphics) {
		graphics.setColor(backgroundColor);
		ShapeRenderer.fill(background);

		for (RigidBody body : entities) {
			drawBody(body, graphics);
		}
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
}