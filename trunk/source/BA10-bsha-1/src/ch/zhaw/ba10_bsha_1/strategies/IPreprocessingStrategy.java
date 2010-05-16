package ch.zhaw.ba10_bsha_1.strategies;


import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Interface to describe a strategy preprocessing a series of {@link TouchPoint}s enclosed
 * in a {@link MicroGesture} container and returning the processed points as one.  
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public interface IPreprocessingStrategy extends IStrategy {
	
	/**
	 * Process the {@link TouchPoint}s of the given {@link MicroGesture}
	 * 
	 * @param micro_gesture
	 * @return
	 */
	public MicroGesture process(MicroGesture micro_gesture);
}
