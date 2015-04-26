public class RoomGesture {
	public enum Type {
		GRAB,
		CIRCLE
	};

	public static RoomGesture makeGrabGesture() {
		return new RoomGesture(Type.GRAB,false);
	}

	public static RoomGesture makeCircleGesture(boolean isClockwise) {
		return new RoomGesture(Type.CIRCLE, isClockwise);
	}

	private Type type;
	private boolean isClockwise;

	public RoomGesture(Type type, boolean isClockwise) {
		this.type = type;
		this.isClockwise = isClockwise;
	}

	public Type getType() {
		return type;
	}

	public boolean getIsClockwise() {
		return isClockwise;
	}
}
