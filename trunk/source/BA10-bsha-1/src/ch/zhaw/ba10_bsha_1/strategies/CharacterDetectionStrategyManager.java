package ch.zhaw.ba10_bsha_1.service;


public class CharacterDetectionStrategyManager extends StrategyManager<ICharacterDetectionStrategy> {
	
	
	private static CharacterDetectionStrategyManager instance;
	
	public static CharacterDetectionStrategyManager getInstance() {
		if (instance == null) {
			instance = new CharacterDetectionStrategyManager();
		}
		return instance;
	}
	
	
	protected void initManager() {
		addStrategy(new CharacterDetectionStrategyNone());
		addStrategy(new CharacterDetectionStrategyGraph());
	}
}
