package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.Character;


/**
 * Interface to describe a strategy postprocessing a series of detected {@link Character}s.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public interface IPostprocessingStrategy extends IStrategy {
	
	/**
	 * Process the given {@link Character}s
	 * 
	 * @param chars
	 * @return
	 */
	public Collection<Character> process(Collection<Character> chars);
}
