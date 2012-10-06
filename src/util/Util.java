package util;

import org.lwjgl.Sys;

public class Util {
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
}
