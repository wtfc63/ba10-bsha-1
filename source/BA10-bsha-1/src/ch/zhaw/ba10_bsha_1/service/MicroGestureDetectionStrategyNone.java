package ch.zhaw.ba10_bsha_1.service;


import java.util.ArrayList;
import java.util.Collection;


public class MicroGestureDetectionStrategyNone implements IMicroGestureDetectionStrategy {

	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> micro_gestures) {
		ArrayList<MicroGesture> result = new ArrayList<MicroGesture>();
		for (MicroGesture mg : micro_gestures) {
			result.add(new MicroGesture(mg.getPoints()));
		}
		return result;
	}

	@Override
	public void setFieldHeight(float fieldHeight) {}

	public String toString() {
		return "None";
	}
}
