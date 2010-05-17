package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import ch.zhaw.ba10_bsha_1.StrategyArgument;


/**
 * Abstract Base Class for strategies. Takes care of the management of the strategy's arguments.
 * Derived classes need to implement the methods used to initialize its arguments and its 
 * name and description getters.
 * 
 * @see ch.zhaw.ba10_bsha_1.strategies.PreprocessingStrategyNormalizePoints
 * @see ch.zhaw.ba10_bsha_1.strategies.PathSmoothingStrategyAverage
 * @see ch.zhaw.ba10_bsha_1.strategies.PathSmoothingStrategySpline
 * @see ch.zhaw.ba10_bsha_1.strategies.MicroGestureDetectionStrategyNone
 * @see ch.zhaw.ba10_bsha_1.strategies.MicroGestureDetectionStrategyRemoveTiny
 * @see ch.zhaw.ba10_bsha_1.strategies.MicroGestureDetectionStrategyCombine
 * @see ch.zhaw.ba10_bsha_1.strategies.MicroGestureDetectionStrategyPrediction
 * @see ch.zhaw.ba10_bsha_1.strategies.MicroGestureDetectionStrategyCurvature
 * @see ch.zhaw.ba10_bsha_1.strategies.MicroGestureDetectionStrategyEdges
 * @see ch.zhaw.ba10_bsha_1.strategies.MicroGestureDetectionStrategyCircles
 * @see ch.zhaw.ba10_bsha_1.strategies.MicroGestureDetectionStrategyHalfCircle
 * @see ch.zhaw.ba10_bsha_1.strategies.CharacterDetectionStrategyNone
 * @see ch.zhaw.ba10_bsha_1.strategies.CharacterDetectionStrategyDummyGraph
 * @see ch.zhaw.ba10_bsha_1.strategies.CharacterDetectionStrategyGraph
 * @see ch.zhaw.ba10_bsha_1.strategies.PostprocessingStrategyNone
 * 
 * @author Julian Hanhart, Domink Giger
 */
public abstract class BaseStrategy implements IStrategy {

	
	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private Hashtable<String, StrategyArgument> arguments;
	protected boolean enabled;

	
	//---------------------------------------------------------------------------
	// Constructor
	//---------------------------------------------------------------------------
	
	
	public BaseStrategy() {
		arguments = new Hashtable<String, StrategyArgument>();
		enabled   = true;
		initialize();
	}

	
	//---------------------------------------------------------------------------
	// Abstract methods to be implemented
	//---------------------------------------------------------------------------
	
	
	/**
	 * Initialize the strategy's {@link StrategyArgument}s
	 */
	@Override
	public abstract void initialize();
	
	/**
	 * Return the strategy's name
	 * 
	 * @return
	 */
	protected abstract String getStrategyName();
	
	/**
	 * Return a description of the strategy
	 * 
	 * @return
	 */
	protected abstract String getStrategyDescription();

	
	//---------------------------------------------------------------------------
	// Getter-/Setter-methods
	//---------------------------------------------------------------------------
	
	
	@Override
	public String toString() {
		return getStrategyName();
	}

	@Override
	public String getDescription() {
		return getStrategyDescription();
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void setEnabled(boolean enable) {
		enabled = enable;
	}
	
	@Override
	public boolean hasArgument(String name) {
		return arguments.containsKey(name.toLowerCase());
	}

	@Override
	public StrategyArgument getArgument(String name) {
		return arguments.get(name.toLowerCase());
	}

	@Override
	public void setArgument(StrategyArgument arg) {
		if ((arg != null) && arg.getStrategyName().equalsIgnoreCase(getStrategyName())) {
			if (arg.getArgumentName().equalsIgnoreCase("enabled")) {
				enabled = !arg.getArgumentValue().equalsIgnoreCase("false");
			} else if (arguments.containsKey(arg.getArgumentName().toLowerCase())) {
				arguments.get(arg.getArgumentName().toLowerCase()).setArgumentValue(arg.getArgumentValue());
			}
		}
	}
	
	@Override
	public void addArgument(StrategyArgument arg) {
		if (arg != null) {
			arguments.put(arg.getArgumentName().toLowerCase(), arg);
		}
	}

	/**
	 * Returns a Collection of all the strategy's {@link StrategyArgument}s
	 * 
	 * @return
	 */
	@Override
	public Collection<StrategyArgument> getConfiguration() {
		ArrayList<StrategyArgument> config = new ArrayList<StrategyArgument>(arguments.values());
		if (!arguments.containsKey("enabled")) {
			config.add(0, new StrategyArgument(
					getStrategyName(), "enabled", Boolean.toString(enabled), "Is strategy enabled or not"));
		}
		return config;
	}
}
