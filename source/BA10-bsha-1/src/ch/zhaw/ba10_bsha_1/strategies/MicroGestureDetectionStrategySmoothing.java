package ch.zhaw.ba10_bsha_1.strategies;

import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.service.*;

public class MicroGestureDetectionStrategySmoothing extends BaseStrategy implements IMicroGestureDetectionStrategy {


	@Override
	protected void initArguments() {}
	
	@Override
	protected String getStrategyDescription() {
		return "Smoothing";
	}

	@Override
	protected String getStrategyName() {
		return "Smoothing";
	}
	
	
	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> microGestures) {
		
		IPreprocessingStrategy smoother = new PathSmoothingStrategySpline();
		ArrayList<MicroGesture> result = new ArrayList<MicroGesture>(microGestures);
		
		for (MicroGesture m : result) {
			smoother.process(m);
		}
		
		return result;
	}
}
