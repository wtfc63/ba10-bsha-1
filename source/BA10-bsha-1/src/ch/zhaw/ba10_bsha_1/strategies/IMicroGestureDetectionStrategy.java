package ch.zhaw.ba10_bsha_1.service;


import java.util.Collection;


public interface IMicroGestureDetectionStrategy extends IStrategy {
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> micro_gestures);
}
