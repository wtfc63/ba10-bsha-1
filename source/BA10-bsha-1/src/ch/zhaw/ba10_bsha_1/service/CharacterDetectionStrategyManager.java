package ch.zhaw.ba10_bsha_1.service;


import java.util.Collection;
import java.util.Hashtable;


public class CharacterDetectionStrategyManager {

	
	private static CharacterDetectionStrategyManager instance;
	private Hashtable<String, ICharacterDetectionStrategy> strategies;
	
	
	private CharacterDetectionStrategyManager() {
		strategies = new Hashtable<String, ICharacterDetectionStrategy>();
		addStrategy(new CharacterDetectionStrategyNone());
		addStrategy(new CharacterDetectionStrategyGraph());
	}
	
	public static CharacterDetectionStrategyManager getInstance() {
		if (instance == null) {
			instance = new CharacterDetectionStrategyManager();
		}
		return instance;
	}
	
	
	public ICharacterDetectionStrategy getStrategy(String name) {
		return strategies.get(name);
	}
	
	public String[] getStrategyList() {
		String[] result = new String[strategies.keySet().size()]; 
		return strategies.keySet().toArray(result);
	}
	
	public void addStrategy(ICharacterDetectionStrategy strategy) {
		strategies.put(strategy.toString(), strategy);
	}
	
	public void addStrategies(Collection<ICharacterDetectionStrategy> strategies) {
		for (ICharacterDetectionStrategy strategy : strategies) {
			this.strategies.put(strategy.toString(), strategy);
		}
	}
	
	public void remStrategy(ICharacterDetectionStrategy strategy) {
		strategies.remove(strategy.toString());
	}
	
	public void remStrategies(Collection<ICharacterDetectionStrategy> strategies) {
		for (ICharacterDetectionStrategy strategy : strategies) {
			this.strategies.remove(strategy.toString());
		}
	}
}
