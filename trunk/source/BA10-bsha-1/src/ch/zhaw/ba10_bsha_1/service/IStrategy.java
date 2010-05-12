package ch.zhaw.ba10_bsha_1.service;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.StrategyArgument;


public interface IStrategy {
	public String toString();
	public String getDescription();
	public Collection<StrategyArgument> getConfiguration();
	public StrategyArgument getArgument(String name);
	public void setArgument(StrategyArgument arg);
}
