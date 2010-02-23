package ch.zhaw.ba10_bsha_1.fingerpad;


import java.util.ArrayList;
import java.util.Collection;


public class MicroGestureDetectionStrategyNone implements IMicroGestureDetectionStrategy {

	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<TouchPoint> points) {
		ArrayList<MicroGesture> result = new ArrayList<MicroGesture>();
		result.add(new MicroGesture(points));
		return result;
	}

	@Override
	public boolean validateMicroGesture(MicroGesture microGesture) {
		return (microGesture.getType() == MicroGesture.TYPE_UNKNOWN);
	}

	public String toString() {
		return "None";
	}
}
