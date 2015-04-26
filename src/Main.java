import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

import java.io.IOException;

class Main implements GestureListener {
	LeapListener leapListener;
	LEDSystem ledSystem;

	public Main() {
		leapListener = new LeapListener(this);
		ledSystem = new LEDSystem();
	}

	public void finish() {
		leapListener.detachController();
	}

	@Override
	public void onGesture(RoomGesture gesture) {
		System.out.println("Got a gesture callback");
		if (gesture.getType() == RoomGesture.Type.GRAB) {
			ledSystem.nextMode();
		} else if (gesture.getType() == RoomGesture.Type.CIRCLE) {
			if (ledSystem.getMode() == LEDSystem.Mode.SOLID_COLOR) {
				if (gesture.getIsClockwise()) {
					ledSystem.changeColor(1);
				} else {
					ledSystem.changeColor(-1);
				}
			}
		}
	}

    public static void main(String[] args) {
		Main main = new Main();

        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Trigger controller detachment when done
        main.finish();
	}
}
