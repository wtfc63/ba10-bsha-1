package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.service.MicroGesture;


public class MicroGestureDetectionStrategyRemoveTiny extends BaseStrategy implements IMicroGestureDetectionStrategy {

	
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "RemoveTiny";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Remove tiny MicroGestures";
	} 
	
	
	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> microGestures) {
		
		ArrayList<MicroGesture> remove = new ArrayList<MicroGesture>();
		for (MicroGesture m : microGestures) {
			// TODO CHECK THIS TOLERANCE (SHOULD BE IMPROVED)
			if (m.getPoints().size() < 5) {
				remove.add(m);
			}
		}
		microGestures.removeAll(remove);
		return microGestures;
	}
}
