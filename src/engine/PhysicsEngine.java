package engine;

import java.util.List;

import entities.Entity;

public class PhysicsEngine {
	private List<Entity> entities;
	
	public PhysicsEngine(List<Entity> entities) {
		this.entities = entities;
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public void update(int delta) {
		
	}
}
