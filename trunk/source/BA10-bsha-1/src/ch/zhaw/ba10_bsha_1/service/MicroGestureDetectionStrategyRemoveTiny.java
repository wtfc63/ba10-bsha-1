package ch.zhaw.ba10_bsha_1.service;

import java.util.ArrayList;
import java.util.Collection;

public class MicroGestureDetectionStrategyRemoveTiny implements
		IMicroGestureDetectionStrategy {

	@Override
	public Collection<MicroGesture> detectMicroGestures(
			Collection<MicroGesture> microGestures) {
		
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

	@Override
	public void setFieldHeight(float fieldHeight) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		return "Remove tiny MicroGestures.";
	}

}
