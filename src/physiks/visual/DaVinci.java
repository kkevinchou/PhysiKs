package physiks.visual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;

public class DaVinci {
	private static DaVinci instance = new DaVinci();
	private Random r;
	private List<Color> colors;

	private DaVinci() {
		r = new Random(System.currentTimeMillis());
		
		// Color palette, should move this to a separate static class later
		colors = new ArrayList<Color>();
		colors.add(new Color(255, 226, 0));
		colors.add(new Color(0, 216, 57));
		colors.add(new Color(255, 35, 0));
		colors.add(new Color(81, 6, 206));
		colors.add(new Color(161, 103, 0));
		colors.add(new Color(255, 57, 0));
		colors.add(new Color(253, 115, 157));
		colors.add(new Color(249, 255, 208));
		colors.add(new Color(2, 40, 109));
	}
	
	public Color getRandomColor() {
		int randomOffset = r.nextInt(colors.size());
		return colors.get(randomOffset);
	}
	
	public static DaVinci getInstance() {
		return instance;
	}
}
