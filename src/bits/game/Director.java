package bits.game;

import bits.entity.Bit;

public class Director {
	Bit e;
	
	public Director() {
		e = new Bit(0, 0, 50, 50);
	}
	
	public void update(float dt) {
		e.update(0);
	}
}
