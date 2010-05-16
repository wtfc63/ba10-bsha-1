package ch.zhaw.ba10_bsha_1.strategies;


/**
 * Implementation of a manager for object implementing {@link IPreprocessingStrategy}.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class PreprocessingStrategyManager extends StrategyManager<IPreprocessingStrategy> {

	
	//---------------------------------------------------------------------------
	// Constructor and implementation of a Singleton pattern
	//---------------------------------------------------------------------------
	
	
	private static PreprocessingStrategyManager instance;
	
	
	private PreprocessingStrategyManager() {
		super();
	}
	
	
	public static PreprocessingStrategyManager getInstance() {
		if (instance == null) {
			instance = new PreprocessingStrategyManager();
		}
		return instance;
	}

	
	//---------------------------------------------------------------------------
	// Implementation of the abstract method
	//---------------------------------------------------------------------------
	

	/**
	 * Initialization of the {@link IPreprocessingStrategy} objects to be managed
	 */
	protected void initManager() {
		addStrategy(new PreprocessingStrategyNormalizePoints());
		addStrategy(new PathSmoothingStrategySpline());
		addStrategy(new PathSmoothingStrategyAverage());
	}
}
