package bits.util;


public class Random {
	private static final Random instance = new Random();
	private java.util.Random r;
	
	private Random() {
		r = new java.util.Random();
		r.setSeed(Util.getTime());
	}
	
	public static Random getInstance() {
		return instance;
	}
	
	public float floatInRange(float min, float max) {
		return r.nextFloat() * (max - min) + min;
	}
}
