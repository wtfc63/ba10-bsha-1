package ch.zhaw.ba10_bsha_1.strategies;


/**
 * Implementation of a manager for object implementing {@link ICharacterDetectionStrategy}.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class CharacterDetectionStrategyManager extends StrategyManager<ICharacterDetectionStrategy> {

	
	//---------------------------------------------------------------------------
	// Constructor and implementation of a Singleton pattern
	//---------------------------------------------------------------------------
	
	
	private static CharacterDetectionStrategyManager instance;
	
	
	private CharacterDetectionStrategyManager() {
		super();
	}
	
	
	public static CharacterDetectionStrategyManager getInstance() {
		if (instance == null) {
			instance = new CharacterDetectionStrategyManager();
		}
		return instance;
	}

	
	//---------------------------------------------------------------------------
	// Implementation of the abstract method
	//---------------------------------------------------------------------------
	

	/**
	 * Initialization of the {@link ICharacterDetectionStrategy} objects to be managed
	 */
	protected void initManager() {
		addStrategy(new CharacterDetectionStrategyNone());
		addStrategy(new CharacterDetectionStrategyGraph());
		addStrategy(new CharacterDetectionStrategyDummyGraph());
	}
}
