package ch.zhaw.ba10_bsha_1.strategies;

import java.util.Collection;
import java.util.Hashtable;

import ch.zhaw.ba10_bsha_1.StrategyArgument;

public abstract class StrategyManager<S extends IStrategy> {

	
	protected Hashtable<String, S> strategies;
	
	
	protected StrategyManager() {
		strategies = new Hashtable<String, S>();
		initManager();
	}
	protected abstract void initManager();
	
	
	public S getStrategy(String name) {
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
	
	public void addStrategy(S strategy) {
		if (strategy != null) {
			strategies.put(strategy.toString().toLowerCase(), strategy);
		}
	}
	
	public void addStrategies(Collection<S> strategies) {
		for (S strategy : strategies) {
			this.strategies.put(strategy.toString().toLowerCase(), strategy);
		}
	}
	
	public void remStrategy(S strategy) {
		strategies.remove(strategy.toString().toLowerCase());
	}
	
	public void remStrategies(Collection<S> strategies) {
		for (S strategy : strategies) {
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
