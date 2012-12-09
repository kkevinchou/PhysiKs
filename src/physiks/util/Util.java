package physiks.util;

import org.lwjgl.Sys;

public class Util {
	private static final float allowableRelativeError = 0.001f;
	
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public static boolean relativeEquals(float a, float b) {
		if (a == b) {
			return true;
		}
		
		float relativeError = Math.abs((a - b) / b);
		
		return (relativeError <= allowableRelativeError);
	}
	
	public static boolean epsilonEquals(float a, float b, float epsilon) {
		return Math.abs(a - b) < Math.abs(epsilon);
	}
}
