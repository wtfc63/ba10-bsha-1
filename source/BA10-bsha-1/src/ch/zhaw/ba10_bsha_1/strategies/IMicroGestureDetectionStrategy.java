package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.service.MicroGesture;


public interface IMicroGestureDetectionStrategy extends IStrategy {
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> micro_gestures);
}
