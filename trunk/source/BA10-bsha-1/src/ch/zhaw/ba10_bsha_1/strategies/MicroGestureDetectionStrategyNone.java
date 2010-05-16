package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Example of an implementation of {@link IMicroGestureDetectionStrategy} doing nothing 
 * except returning the {@link MicroGesture}s given to it.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class MicroGestureDetectionStrategyNone extends BaseStrategy implements IMicroGestureDetectionStrategy {


	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	
	
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "None";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Return given MicroGestures unchanged";
	} 


	//---------------------------------------------------------------------------
	// Implementation of IMicroGestureDetectionStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Detect {@link MicroGesture}s in the {@link TouchPoint}s of the given {@link MicroGesture}s
	 * (Does nothing except returning the given {@link MicroGesture}s)
	 * 
	 * @param micro_gestures
	 * @return
	 */
	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> micro_gestures) {
		return micro_gestures;
	}
}
