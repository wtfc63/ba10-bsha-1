package ch.zhaw.ba10_bsha_1.fingerpad;


import java.util.Collection;


public interface IMicroGestureDetectionStrategy {
	public Collection<MicroGesture> detectMicroGestures(Collection<TouchPoint> points);
	public boolean validateMicroGesture(MicroGesture micro_gesture);
	public String toString();
}
