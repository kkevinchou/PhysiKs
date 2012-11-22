package physiks.visual;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

public class DaVinci {
	private static DaVinci instance = new DaVinci();
	
	private List<Color> colors;

	private DaVinci() {
		// Color palette, should move this to a separate static class later
		colors = new ArrayList<Color>();
		colors.add(new Color(245, 162, 63));
		colors.add(new Color(168, 112, 44));
		colors.add(new Color(109, 190, 194));
		colors.add(new Color(255, 245, 81));
		colors.add(new Color(215, 20, 20));
		colors.add(new Color(225, 123, 10));
		colors.add(new Color(102, 51, 84));
		colors.add(new Color(174, 33, 67));
		colors.add(new Color(51, 51, 85));
		colors.add(new Color(17, 68, 85));
		colors.add(new Color(7, 155, 123));
	}
	
	public Color getRandomColor() {
		int randomOffset = (int)(Math.random() * colors.size());
		return colors.get(randomOffset);
	}
	
	public static DaVinci getInstance() {
		return instance;
	}
}
