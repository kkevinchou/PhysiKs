package managers;

import java.util.ArrayList;
import java.util.List;

import entities.Entity;

public class EntityManager {
	private static EntityManager instance;
	private List<Entity> entities;
	
	private EntityManager() {
		entities = new ArrayList<Entity>();
	}
	
	public static EntityManager getInstance() {
		if (instance == null) {
			instance = new EntityManager();
		}
		return instance;
	}
	
	public void add(Entity e) {
		entities.add(e);
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
}
