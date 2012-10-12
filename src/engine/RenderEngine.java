package engine;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import physiks.PhysiKsSim;

import util.Util;

import entities.RigidBody;
import geometry.Vector2D;

public class RenderEngine {
	private List<RigidBody> entities;
	
	public RenderEngine(List<RigidBody> rigidBodies) {
		this.entities = rigidBodies;
	}
	
	private void drawBody(RigidBody r, Graphics graphics) {
		Vector2D position = r.getPosition();
		float radius = r.getRadius();
		
		graphics.draw(new Circle(position.getX(), PhysiKsSim.HEIGHT - position.getY(), radius));
	}
	
	public void update(Graphics graphics) {
		for (RigidBody body : entities) {
			drawBody(body, graphics);
		}
	}
}
