package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Implementation of {@link IMicroGestureDetectionStrategy} which removes all {@link MicroGesture}s that do not
 * meet its size requirements (that do not have enough points).
 * 
 * @author Dominik Giger, Julian Hanhart
 */
public class MicroGestureDetectionStrategyRemoveTiny extends BaseStrategy implements IMicroGestureDetectionStrategy {


	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	
	
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "RemoveTiny";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Remove tiny MicroGestures";
	} 


	//---------------------------------------------------------------------------
	// Implementation of IMicroGestureDetectionStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Remove {@link MicroGesture}s that do not have enough points
	 * 
	 * @param micro_gestures
	 * @return
	 */
	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> microGestures) {
		
		ArrayList<MicroGesture> result = new ArrayList<MicroGesture>();
		for (MicroGesture m : microGestures) {
			// TODO CHECK THIS TOLERANCE (SHOULD BE IMPROVED)
			if (m.getPoints().size() > 3) {
				result.add(m);
			}
		}

		return result;
	}
}
