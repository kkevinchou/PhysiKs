package bits.game;

import bits.util.Util;

public class DeltaTracker {
	long lastTick;
	
	public DeltaTracker() {
		lastTick = Util.getTime();
	}
	
	public void init() {
		lastTick = Util.getTime();
	}
	
	public long getDelta() {
		long currentTick = Util.getTime();
		long delta = currentTick - lastTick;
		lastTick = currentTick;
		
		return delta;
	}
}
