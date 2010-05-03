package ch.zhaw.ba10_bsha_1.service;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.TouchPoint;


public interface IMicroGestureDetectionStrategy {
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> micro_gestures);
	public void setFieldHeight(float field_height);
	public String toString();
}
