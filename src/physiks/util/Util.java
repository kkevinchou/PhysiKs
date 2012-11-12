package physiks.util;

import org.lwjgl.Sys;

public class Util {
	private static final float allowableRelativeError = 0.001f;
	
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public static boolean relativeEquals(float a, float b) {
		float relativeError = Math.abs((a - b) / b);
		return (relativeError <= allowableRelativeError);
	}
}
