package engine;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import util.Util;

import entities.Entity;
import geometry.Vector2D;

public class RenderEngine {
	private List<Entity> entities;
	
	public RenderEngine(List<Entity> entities) {
		this.entities = entities;
	}
	
	public void update(Graphics graphics) {
		for (Entity entity : entities) {
			Vector2D position = entity.getPosition();
			float radius = entity.getRadius();
			
			graphics.draw(new Circle(position.getX(), position.getY(), radius));
		}
	}
}
