package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Interface to describe a strategy detecting {@link MicroGesture}s in the {@link TouchPoint}s of 
 * the given {@link MicroGesture}s and returning the detected {@link MicroGesture}s.  
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public interface IMicroGestureDetectionStrategy extends IStrategy {
	
	/**
	 * Detect {@link MicroGesture}s in the {@link TouchPoint}s of the given {@link MicroGesture}s
	 * 
	 * @param micro_gestures
	 * @return
	 */
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> micro_gestures);
}
