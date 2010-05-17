package ch.zhaw.ba10_bsha_1.strategies;


import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Implementation of {@link IPreprocessingStrategy} doing nothing, except 
 * returning the {@link Character}s it was given.
 * 
 * @author Dominik Giger, Julian Hanhart
 */
public class PreprocessingStrategyNone extends BaseStrategy implements IPreprocessingStrategy {

	
	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	
	
	@Override
	public void initialize() {}

	@Override
	protected String getStrategyName() {
		return "None";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Return given MicroGesture with it's points unchanged";
	} 


	//---------------------------------------------------------------------------
	// Implementation of IPreprocessingStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Process the {@link TouchPoint}s of the given {@link MicroGesture}
	 * (Does nothing except returning them unchanged)
	 * 
	 * @param micro_gesture
	 * @return
	 */
	@Override
	public MicroGesture process(MicroGesture micro_gesture) {
		return micro_gesture;
	}
}
