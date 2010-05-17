package ch.zhaw.ba10_bsha_1.strategies;

import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.service.*;


/**
 * Implementation of {@link IMicroGestureDetectionStrategy} which applies an {@link IPreprocessingStrategy} to all
 * given {@link MicroGesture}s to smooth their points' path.
 * 
 * @author Dominik Giger, Julian Hanhart
 */
public class MicroGestureDetectionStrategySmoothing extends BaseStrategy implements IMicroGestureDetectionStrategy {


	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	
	
	@Override
	public void initialize() {}
	
	@Override
	protected String getStrategyDescription() {
		return "Smoothing";
	}

	@Override
	protected String getStrategyName() {
		return "Smoothing";
	}


	//---------------------------------------------------------------------------
	// Implementation of IMicroGestureDetectionStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Smooth path of all given {@link MicroGesture}s
	 * 
	 * @param micro_gestures
	 * @return
	 */
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
