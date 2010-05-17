package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.StrategyArgument;


/**
 * Interface defining a strategy. Note that the actual type of a strategy and its processing method are
 * described through a series of additional interfaces derived from this one, since their processing
 * methods take different types of arguments and because they have different return types.
 * 
 * @see ch.zhaw.ba10_bsha_1.strategies.IPreprocessingStrategy
 * @see ch.zhaw.ba10_bsha_1.strategies.IMicroGestureDetectionStrategy
 * @see ch.zhaw.ba10_bsha_1.strategies.ICharacterDetectionStrategy
 * @see ch.zhaw.ba10_bsha_1.strategies.IPostprocessingStrategy
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public interface IStrategy {
	
	public void initialize();
	
	public String toString();
	public String getDescription();
	
	public boolean isEnabled();
	public void setEnabled(boolean enable);
	
	public boolean hasArgument(String name);
	public StrategyArgument getArgument(String name);
	public void setArgument(StrategyArgument arg);
	public void addArgument(StrategyArgument arg);
	
	public Collection<StrategyArgument> getConfiguration();
}
