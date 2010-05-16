package ch.zhaw.ba10_bsha_1.strategies;


/**
 * Implementation of a manager for object implementing {@link IMicroGestureDetectionStrategy}.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class MicroGestureDetectionStrategyManager extends StrategyManager<IMicroGestureDetectionStrategy> {


	//---------------------------------------------------------------------------
	// Constructor and implementation of a Singleton pattern
	//---------------------------------------------------------------------------
	
	
	private static MicroGestureDetectionStrategyManager instance;
	
	
	private MicroGestureDetectionStrategyManager() {
		super();
	}
	
	
	public static MicroGestureDetectionStrategyManager getInstance() {
		if (instance == null) {
			instance = new MicroGestureDetectionStrategyManager();
		}
		return instance;
	}
	

	//---------------------------------------------------------------------------
	// Implementation of the abstract method
	//---------------------------------------------------------------------------
	

	/**
	 * Initialization of the {@link IMicroGestureDetectionStrategy} objects to be managed
	 */
	@SuppressWarnings("deprecation")
	protected void initManager() {
		addStrategy(new MicroGestureDetectionStrategyNone());
		addStrategy(new MicroGestureDetectionStrategyEdges());
		addStrategy(new MicroGestureDetectionStrategyCircles());
		addStrategy(new MicroGestureDetectionStrategyRemoveTiny());
		addStrategy(new MicroGestureDetectionStrategyLines());
		addStrategy(new MicroGestureDetectionStrategyHalfCircle());
		addStrategy(new MicroGestureDetectionStrategyPrediction());
		addStrategy(new MicroGestureDetectionStrategyCurvature());
		addStrategy(new MicroGestureDetectionStrategySmoothing());
	}
}
