package bits.entity;

import bits.component.VisualComponent;

public class Bit extends Entity {
	private VisualComponent visualComponent;

	public Bit(float x, float y, float width, float height) {
		super(x, y, width, height);
		
		visualComponent = new VisualComponent(this);
	}
	
	public void update(long dt) {
		visualComponent.update();
	}
}
