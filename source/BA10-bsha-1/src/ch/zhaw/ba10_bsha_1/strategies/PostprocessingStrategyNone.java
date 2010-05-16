package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.Character;


/**
 * Example of an implementation of {@link IPostprocessingStrategy} doing nothing 
 * except returning the {@link Character}s given to it.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class PostprocessingStrategyNone extends BaseStrategy implements IPostprocessingStrategy {

	
	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	
	
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "None";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Return Characters unchanged";
	} 


	//---------------------------------------------------------------------------
	// Implementation of IPostprocessingStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Process the given {@link Character}s
	 * (Does nothing except returning the given {@link Character}s)
	 * 
	 * @param chars
	 * @return
	 */
	@Override
	public Collection<Character> process(Collection<Character> chars) {
		return chars;
	}
}
