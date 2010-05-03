package ch.zhaw.ba10_bsha_1.service;


import java.util.Collection;
import java.util.Hashtable;


public class PreprocessingStrategyManager {

	
	private static PreprocessingStrategyManager instance;
	private Hashtable<String, IPreprocessingStrategy> strategies;
	
	
	private PreprocessingStrategyManager() {
		strategies = new Hashtable<String, IPreprocessingStrategy>();
		addStrategy(new PathSmoothingStrategySpline());
		addStrategy(new PathSmoothingStrategyAverage());
	}
	
	public static PreprocessingStrategyManager getInstance() {
		if (instance == null) {
			instance = new PreprocessingStrategyManager();
		}
		return instance;
	}
	
	
	public IPreprocessingStrategy getStrategy(String name) {
		return strategies.get(name);
	}
	
	public String[] getStrategyList() {
		String[] result = new String[strategies.keySet().size()]; 
		return strategies.keySet().toArray(result);
	}
	
	public void addStrategy(IPreprocessingStrategy strategy) {
		strategies.put(strategy.toString(), strategy);
	}
	
	public void addStrategies(Collection<IPreprocessingStrategy> strategies) {
		for (IPreprocessingStrategy strategy : strategies) {
			this.strategies.put(strategy.toString(), strategy);
		}
	}
	
	public void remStrategy(IPreprocessingStrategy strategy) {
		strategies.remove(strategy.toString());
	}
	
	public void remStrategies(Collection<IPreprocessingStrategy> strategies) {
		for (IPreprocessingStrategy strategy : strategies) {
			this.strategies.remove(strategy.toString());
		}
	}
}
