package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Example of an implementation of {@link ICharacterDetectionStrategy} doing nothing, except 
 * returning the "Null"-{@link Character}.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class CharacterDetectionStrategyNone extends BaseStrategy implements	ICharacterDetectionStrategy {


	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	

	@Override
	public void initialize() {}

	@Override
	protected String getStrategyName() {
		return "None";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Return Null-Character with all given MicroGestures attached";
	}


	//---------------------------------------------------------------------------
	// Implementation of ICharacterDetectionStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Detect {@link Character}s out of the given {@link MicroGesture}s
	 * (Does nothing except returning the "Null"-{@link Character})
	 * 
	 * @param micro_gestures
	 * @return
	 */
	@Override
	public Collection<Character> detectCharacter(Collection<MicroGesture> micro_gestures) {
		Character no_char = new Character(micro_gestures, '\0', 0);
		ArrayList<Character> chars = new ArrayList<Character>();
		chars.add(no_char);
		return chars;
	}
}
