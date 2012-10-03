package bits.game;

public class IntGenerator {
	private int nextInt;
	
	public IntGenerator() {
		nextInt = 0;
	}
	
	public int nextInt() {
		return nextInt++;
	}
}
