package ch.zhaw.ba10_bsha_1.service;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

public class MicroGestureDetectionStrategyManager {

	
	private static MicroGestureDetectionStrategyManager instance;
	private Hashtable<String, IMicroGestureDetectionStrategy> strategies;
	
	
	private MicroGestureDetectionStrategyManager() {
		strategies = new Hashtable<String, IMicroGestureDetectionStrategy>();
		addStrategy(new MicroGestureDetectionStrategyNone());
		addStrategy(new MicroGestureDetectionStrategyPrediction());
		addStrategy(new MicroGestureDetectionStrategyCurvature());
	}
	
	public static MicroGestureDetectionStrategyManager getInstance() {
		if (instance == null) {
			instance = new MicroGestureDetectionStrategyManager();
		}
		return instance;
	}
	
	public void setFieldHeightInStrategies(float field_height) {
		Enumeration<IMicroGestureDetectionStrategy> elems = strategies.elements();
		while (elems.hasMoreElements()) {
			elems.nextElement().setFieldHeight(field_height);
		}
	}
	
	
	public IMicroGestureDetectionStrategy getStrategy(String name) {
		return strategies.get(name);
	}
	
	public String[] getStrategyList() {
		String[] result = new String[strategies.keySet().size()]; 
		return strategies.keySet().toArray(result);
	}
	
	public void addStrategy(IMicroGestureDetectionStrategy strategy) {
		strategies.put(strategy.toString(), strategy);
	}
	
	public void addStrategies(Collection<IMicroGestureDetectionStrategy> strategies) {
		for (IMicroGestureDetectionStrategy strategy : strategies) {
			this.strategies.put(strategy.toString(), strategy);
		}
	}
	
	public void remStrategy(IMicroGestureDetectionStrategy strategy) {
		strategies.remove(strategy.toString());
	}
	
	public void remStrategies(Collection<IMicroGestureDetectionStrategy> strategies) {
		for (IMicroGestureDetectionStrategy strategy : strategies) {
			this.strategies.remove(strategy.toString());
		}
	}
}
