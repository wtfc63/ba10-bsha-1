package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.service.MicroGesture;


public class MicroGestureDetectionStrategyCombine extends BaseStrategy implements IMicroGestureDetectionStrategy {

	
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "Combine";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Combine MicroGestures where applicable";
	}
	
	
	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> microGestures) {
		// TODO Auto-generated method stub
		return null;
	}
}
