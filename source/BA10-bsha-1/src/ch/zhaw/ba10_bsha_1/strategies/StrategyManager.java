package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;
import java.util.Hashtable;

import ch.zhaw.ba10_bsha_1.StrategyArgument;


/**
 * Abstract Base Class for a class managing objects implementing {@link IStrategy}.
 * 
 * @see ch.zhaw.ba10_bsha_1.strategies.PreprocessingStrategyManager
 * @see ch.zhaw.ba10_bsha_1.strategies.MicroGestureDetectionStrategyManager
 * @see ch.zhaw.ba10_bsha_1.strategies.CharacterDetectionStrategyManager
 * @see ch.zhaw.ba10_bsha_1.strategies.PostprocessingStrategyManager
 *   
 * @author Julian Hanhart, Dominik Giger
 * @param <Strategy>
 */
public abstract class StrategyManager<Strategy extends IStrategy> {


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private Hashtable<String, Strategy> strategies;
	

	//---------------------------------------------------------------------------
	// Constructor
	//---------------------------------------------------------------------------
	
	
	protected StrategyManager() {
		strategies = new Hashtable<String, Strategy>();
		initManager();
	}
	

	//---------------------------------------------------------------------------
	// Abstract method to be implemented
	//---------------------------------------------------------------------------
	
	
	/**
	 * Initialize and add the Strategies it should manage to the manager
	 */
	protected abstract void initManager();
	

	//---------------------------------------------------------------------------
	// Strategy management methods
	//---------------------------------------------------------------------------
	
	
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


	//---------------------------------------------------------------------------
	// StrategyArgument management methods
	//---------------------------------------------------------------------------
	
	
	/**
	 * Get the argument specified in the given {@link StrategyArgument} from the Strategy it specifies
	 * 
	 * @param arg
	 * @return
	 */
	public StrategyArgument getArgument(StrategyArgument arg) {
		return strategies.get(arg.getStrategyName().toLowerCase()).getArgument(arg.getArgumentName());
	}

	/**
	 * Set the argument specified in the given {@link StrategyArgument} at the Strategy it specifies
	 * 
	 * @param arg
	 */
	public void setArgument(StrategyArgument arg) {
		strategies.get(arg.getStrategyName().toLowerCase()).setArgument(arg);
	}
	
	/**
	 * Broadcast {@link StrategyArgument} to all strategies that use the given argument
	 * 
	 * @param arg
	 */
	public void broadcastArgument(StrategyArgument arg) {
		for (Strategy strategy : strategies.values()) {
			if (strategy.hasArgument(arg.getArgumentName())) {
				arg.setStrategyName(strategy.toString());
				strategy.setArgument(arg);
			}
		}
	}
}
