package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.StrategyArgument;


public interface IStrategy {
	public String toString();
	public String getDescription();
	public boolean isEnabled();
	public boolean hasArgument(String name);
	public StrategyArgument getArgument(String name);
	public Collection<StrategyArgument> getConfiguration();
	public void setEnabled(boolean enable);
	public void addArgument(StrategyArgument arg);
	public void setArgument(StrategyArgument arg);
}
