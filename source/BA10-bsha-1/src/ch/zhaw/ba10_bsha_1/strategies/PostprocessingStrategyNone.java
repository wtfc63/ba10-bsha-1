package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.Character;


public class PostprocessingStrategyNone extends BaseStrategy implements IPostprocessingStrategy {

	
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "None";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Return Characters unchanged";
	} 
	
	
	@Override
	public Collection<Character> process(Collection<Character> chars) {
		return chars;
	}
}
