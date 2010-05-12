package ch.zhaw.ba10_bsha_1.service;


import java.util.Collection;


public class MicroGestureDetectionStrategyNone extends BaseStrategy implements IMicroGestureDetectionStrategy {

	
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "None";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Return given MicroGestures unchanged";
	} 
	
	
	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> micro_gestures) {
		return micro_gestures;
	}
}
