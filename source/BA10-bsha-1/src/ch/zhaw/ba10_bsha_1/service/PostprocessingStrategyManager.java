package ch.zhaw.ba10_bsha_1.service;


import java.util.Collection;
import java.util.Hashtable;


public class PostprocessingStrategyManager {

	
	private static PostprocessingStrategyManager instance;
	private Hashtable<String, IPostprocessingStrategy> strategies;
	
	
	private PostprocessingStrategyManager() {
		strategies = new Hashtable<String, IPostprocessingStrategy>();
		addStrategy(new PostprocessingStrategyNone());
	}
	
	public static PostprocessingStrategyManager getInstance() {
		if (instance == null) {
			instance = new PostprocessingStrategyManager();
		}
		return instance;
	}
	
	
	public IPostprocessingStrategy getStrategy(String name) {
		return strategies.get(name);
	}
	
	public String[] getStrategyList() {
		String[] result = new String[strategies.keySet().size()]; 
		return strategies.keySet().toArray(result);
	}
	
	public void addStrategy(IPostprocessingStrategy strategy) {
		strategies.put(strategy.toString(), strategy);
	}
	
	public void addStrategies(Collection<IPostprocessingStrategy> strategies) {
		for (IPostprocessingStrategy strategy : strategies) {
			this.strategies.put(strategy.toString(), strategy);
		}
	}
	
	public void remStrategy(IPostprocessingStrategy strategy) {
		strategies.remove(strategy.toString());
	}
	
	public void remStrategies(Collection<IPostprocessingStrategy> strategies) {
		for (IPostprocessingStrategy strategy : strategies) {
			this.strategies.remove(strategy.toString());
		}
	}
}
