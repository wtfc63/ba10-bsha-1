package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;
import java.util.Hashtable;

import ch.zhaw.ba10_bsha_1.StrategyArgument;


public abstract class StrategyManager<Strategy extends IStrategy> {

	
	protected Hashtable<String, Strategy> strategies;
	
	
	protected StrategyManager() {
		strategies = new Hashtable<String, Strategy>();
		initManager();
	}
	protected abstract void initManager();
	
	
	public Strategy getStrategy(String name) {
		if (strategies.containsKey(name.toLowerCase())) {
			return strategies.get(name.toLowerCase());
		} else {
			return null;
		}
	}
	
	public String[] getStrategyList() {
		String[] result = new String[strategies.keySet().size()]; 
		return strategies.keySet().toArray(result);
	}
	
	public void addStrategy(Strategy strategy) {
		if (strategy != null) {
			strategies.put(strategy.toString().toLowerCase(), strategy);
		}
	}
	
	public void addStrategies(Collection<Strategy> strategies) {
		for (Strategy strategy : strategies) {
			this.strategies.put(strategy.toString().toLowerCase(), strategy);
		}
	}
	
	public void remStrategy(Strategy strategy) {
		strategies.remove(strategy.toString().toLowerCase());
	}
	
	public void remStrategies(Collection<Strategy> strategies) {
		for (Strategy strategy : strategies) {
			this.strategies.remove(strategy.toString().toLowerCase());
		}
	}
	
	public StrategyArgument getArgument(StrategyArgument arg) {
		return strategies.get(arg.getStrategyName().toLowerCase()).getArgument(arg.getArgumentName());
	}
	
	public void setArgument(StrategyArgument arg) {
		strategies.get(arg.getStrategyName().toLowerCase()).setArgument(arg);
	}
}
