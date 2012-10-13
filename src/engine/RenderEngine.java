package engine;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;

import physiks.PhysiKsSim;

import util.Util;

import entities.*;
import geometry.Vector2D;

public class RenderEngine {
	private List<RigidBody> entities;
	
	public RenderEngine(List<RigidBody> rigidBodies) {
		this.entities = rigidBodies;
	}
	
	private void drawBody(RigidBody r, Graphics graphics) {
		Polygon sprite = new Polygon();
		
		if (r instanceof PolyBody) {
			PolyBody body = (PolyBody)r;
			Vector2D position = r.getPosition();
			for (Vector2D point : body.getPoints()) {
				float x = point.getX() + position.getX();
				float y = point.getY() + position.getY();
				sprite.addPoint(x, y);
			}
			sprite.setClosed(true);
		}
		
		graphics.draw(sprite);
	}
	
	public void update(Graphics graphics) {
		for (RigidBody body : entities) {
			drawBody(body, graphics);
		}
	}
}
