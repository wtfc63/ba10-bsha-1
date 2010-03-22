package ch.zhaw.ba10_bsha_1.fingerpad;


public class MicroGestureTester {
	
	
	public static final char MASK_DIRECTION_LEFT   = 1;      //0b00000001
	public static final char MASK_DIRECTION_RIGHT  = 1 << 1; //0b00000010
	public static final char MASK_DIRECTION_UP     = 1 << 2; //0b00000100
	public static final char MASK_DIRECTION_DOWN   = 1 << 3; //0b00001000
	public static final char MASK_DIRECTION_SWITCH = 1 << 4; //0b00010000
	
	private int type;
	private char directionPattern;
	
	
	public MicroGestureTester(int type, char dir_pattern) {
		this.type = type;
		this.directionPattern = dir_pattern;
	}
	
	public MicroGestureTester(String init) {
		String[] parts = init.split(":");
		if (parts.length > 1) {
			this.type = MicroGesture.StrToType(parts[0]);
			this.directionPattern = StrToPattern(parts[1]);
		}
	}
	
	
	public boolean validate(MicroGesture micro_gesture) {
		char dir = 0;
		dir = micro_gesture.directionIsLeft()      ? (char) (dir | MASK_DIRECTION_LEFT)   : dir;
		dir = micro_gesture.directionIsRight()     ? (char) (dir | MASK_DIRECTION_RIGHT)  : dir;
		dir = micro_gesture.directionIsUp()        ? (char) (dir | MASK_DIRECTION_UP)     : dir;
		dir = micro_gesture.directionIsDown()      ? (char) (dir | MASK_DIRECTION_DOWN)   : dir;
		dir = micro_gesture.directionHasSwitched() ? (char) (dir | MASK_DIRECTION_SWITCH) : dir;
		return ((type == micro_gesture.getType()) && ((dir & directionPattern) != 0));
	}
	
	
	public static char StrToPattern(String str) {
		char pattern = 0;
		if (str.contains("l")) {
			pattern |= MASK_DIRECTION_LEFT;
		}
		if (str.contains("r")) {
			pattern |= MASK_DIRECTION_RIGHT;
		}
		if (str.contains("u")) {
			pattern |= MASK_DIRECTION_UP;
		}
		if (str.contains("d")) {
			pattern |= MASK_DIRECTION_DOWN;
		}
		if (str.contains("^")) {
			pattern |= MASK_DIRECTION_SWITCH;
		}
		return pattern;
	}
}
