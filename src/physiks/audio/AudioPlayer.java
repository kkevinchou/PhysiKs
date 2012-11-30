package physiks.audio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class AudioPlayer {
	private boolean enabled = true;
	private static final String audioResourcesFolderPath = "resources/audio";
	private static AudioPlayer instance = new AudioPlayer();
	Map<String, Audio> audioMap;

	private AudioPlayer() {
		audioMap = new HashMap<String, Audio>();
		loadAudioResources();
	}

	public static AudioPlayer getInstance() {
		return instance;
	}

	private void loadAudioResources() {
		File folder = new File(audioResourcesFolderPath);
		File[] files = folder.listFiles();

		for (File file : files) {
			if (file.isFile()) {
				String fileName = file.getName();
				if (fileName.endsWith(".wav")) {
					int extensionIndex = fileName.indexOf(".wav");
					String fileNameWithoutExtension = fileName.substring(0, extensionIndex);
					
					loadWav(fileNameWithoutExtension, file.getPath());
				}
			}
		}
	}

	private void loadWav(String key, String filePath) {
		try {
			Audio audio = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream(filePath));
			audioMap.put(key, audio);
		} catch (IOException e) {
			System.out.println("Audio file: " + filePath + " was not loaded");
			e.printStackTrace();
		}
	}

	public void playWav(String fileName) {
		if (!enabled) {
			return;
		}
		
		Audio audio = audioMap.get(fileName);
		
		if (audio != null) {
			audio.playAsSoundEffect(1f, 0.5f, false);
		} else {
			System.out.println("Audio file: " + fileName + " was not loaded");
		}
	}
	
	public void setSoundsEnabled(boolean enabled) {
		this.enabled = enabled; 
	}
}
