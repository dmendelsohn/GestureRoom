import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.URLConnection;
import java.net.URL;

public class LEDSystem {
	enum RGBColor {RED, GREEN, BLUE, PURPLE, TEAL, YELLOW, WHITE, OFF, PRETTY};
	enum Mode {OFF, SOLID_COLOR, PRETTY};
	private static final RGBColor[] COLORS = {
		RGBColor.RED, 
		RGBColor.PURPLE, 
		RGBColor.BLUE, 
		RGBColor.TEAL, 
		RGBColor.GREEN, 
		RGBColor.YELLOW
	};
	private static final Mode[] MODES = {
		Mode.OFF, 
		Mode.SOLID_COLOR, 
		Mode.PRETTY
	};
	private static final int NUM_COLORS = COLORS.length;
	private static final int NUM_MODES = MODES.length;

	private int colorIndex=0;
	private int modeIndex = 0;
	private double brightness = 1.0;

	public Mode getMode() {
		return MODES[modeIndex];
	}

	public RGBColor getColor() {
		return COLORS[colorIndex];
	}

	public void changeColor(int offset) {
		colorIndex = (colorIndex+offset)%NUM_COLORS;			
		setColor(COLORS[colorIndex]);
	}

	public void nextMode() {
		modeIndex = (modeIndex+1)%NUM_MODES;
		Mode mode = MODES[modeIndex];
		if (mode == Mode.OFF) {
			setColor(RGBColor.OFF);
		} else if (mode == Mode.PRETTY) {
			setColor(RGBColor.PRETTY);
		} else if (mode == Mode.SOLID_COLOR) {
			setColor(COLORS[colorIndex]);
		}
	}

	public void setColor(RGBColor color) {
		System.out.println("Setting color to: " + color.name());
		String urlString = "http://arduino.local/arduino/mode/" + color.name().toLowerCase();
		try {
			URL url = new URL(urlString);
			URLConnection c = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) 
				System.out.println(inputLine);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
