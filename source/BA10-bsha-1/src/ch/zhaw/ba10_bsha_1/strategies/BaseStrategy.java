package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;
import java.util.Hashtable;

import ch.zhaw.ba10_bsha_1.StrategyArgument;


public abstract class BaseStrategy implements IStrategy {
	
	
	protected Hashtable<String, StrategyArgument> arguments;
	protected boolean enabled;
	
	
	public BaseStrategy() {
		arguments = new Hashtable<String, StrategyArgument>();
		enabled   = true;
		initArguments();
	}
	
	protected abstract void initArguments();
	protected abstract String getStrategyName();
	protected abstract String getStrategyDescription();
	
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
	public StrategyArgument getArgument(String name) {
		return arguments.get(name.toLowerCase());
	}

	@Override
	public Collection<StrategyArgument> getConfiguration() {
		return arguments.values();
	}
	
	@Override
	public void setEnabled(boolean enable) {
		enabled = enable;
	}

	@Override
	public void setArgument(StrategyArgument arg) {
		if ((arg != null) 
				&& arg.getStrategyName().equalsIgnoreCase(getStrategyName()) 
				&& arguments.containsKey(arg.getArgumentName().toLowerCase())) {
			arguments.get(arg.getArgumentName().toLowerCase()).setArgumentValue(arg.getArgumentValue());
		}
	}
}
