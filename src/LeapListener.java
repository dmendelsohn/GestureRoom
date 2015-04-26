import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

public class LeapListener extends Listener {
	private GestureListener gestureListener;
	private Controller controller;

	private static final long GESTURE_TIMEOUT = 2000;
	private static final float GRAB_CUTOFF = 0.5f;
	private static final float NO_GRAB_CUTOFF = 0.25f;

	private boolean isGrabbing = false;
	private long lastGesture = System.currentTimeMillis();

	public LeapListener(GestureListener gl) {
		controller = new Controller();
		controller.addListener(this);
		this.gestureListener = gl;
	}

	public void detachController() {
		controller.removeListener(this);
	}

	public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
		// Get the most recent frame and report some basic information
        Frame frame = controller.frame();
		GestureList gestures = frame.gestures();
		for (int i = 0; i < gestures.count(); i++) {
           	Gesture gesture = gestures.get(i);
	        switch (gesture.type()) {
               	case TYPE_CIRCLE:
               	    CircleGesture circle = new CircleGesture(gesture);
					
					// Calculate clock direction using the angle between circle normal and pointable
					String clockwiseness;
					if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2) {
						// Clockwise if angle is less than 90 degrees
						clockwiseness = "clockwise";
					} else {
						clockwiseness = "counterclockwise";
					}

					if (System.currentTimeMillis() - lastGesture > GESTURE_TIMEOUT) {
						if (clockwiseness.equals("clockwise")) {
							gestureListener.onGesture(RoomGesture.makeCircleGesture(true));
							lastGesture = System.currentTimeMillis();
						} else if (clockwiseness.equals("counterclockwise")) {
							gestureListener.onGesture(RoomGesture.makeCircleGesture(false));
							lastGesture = System.currentTimeMillis();;
						}
					}
					break;
				default:
					System.out.println("Unknown gesture type.");
					break;
			}
		}

		Hand hand = frame.hands().leftmost();
		if (!isGrabbing && hand.grabStrength() > GRAB_CUTOFF) {
			System.out.println("found a grab");
			gestureListener.onGesture(RoomGesture.makeGrabGesture());
			isGrabbing = true;
		} else if (isGrabbing && hand.grabStrength() < NO_GRAB_CUTOFF) {
			isGrabbing = false;
		}
	}
}

