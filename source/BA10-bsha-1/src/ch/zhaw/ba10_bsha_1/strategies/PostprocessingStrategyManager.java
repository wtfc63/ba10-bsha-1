package ch.zhaw.ba10_bsha_1.strategies;


/**
 * Implementation of a manager for object implementing {@link IProstprocessingStrategy}.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class PostprocessingStrategyManager extends StrategyManager<IPostprocessingStrategy> {

	
	//---------------------------------------------------------------------------
	// Constructor and implementation of a Singleton pattern
	//---------------------------------------------------------------------------
	
	
	private static PostprocessingStrategyManager instance;
	
	
	private PostprocessingStrategyManager() {
		super();
	}
	
	
	public static PostprocessingStrategyManager getInstance() {
		if (instance == null) {
			instance = new PostprocessingStrategyManager();
		}
		return instance;
	}
	
	
	//---------------------------------------------------------------------------
	// Implementation of the abstract method
	//---------------------------------------------------------------------------
	

	/**
	 * Initialization of the {@link IPostprocessingStrategy} objects to be managed
	 */
	protected void initManager() {
		addStrategy(new PostprocessingStrategyNone());
	}
}
