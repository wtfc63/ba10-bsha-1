package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Interface to describe an {@link IStrategy} detecting {@link Character}s out of the given {@link MicroGesture}s
 * and returning the detected {@link Character}s.  
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public interface ICharacterDetectionStrategy extends IStrategy {
	
	/**
	 * Detect {@link Character}s out of the given {@link MicroGesture}s
	 * 
	 * @param micro_gestures
	 * @return
	 */
	public Collection<Character> detectCharacter(Collection<MicroGesture> micro_gestures);
}
