package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


public class CharacterDetectionStrategyNone extends BaseStrategy implements	ICharacterDetectionStrategy {

	
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "None";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Return Null-Character with all given MicroGestures attached";
	}
	
	
	@Override
	public Collection<Character> detectCharacter(Collection<MicroGesture> micro_gestures) {
		Character no_char = new Character(micro_gestures, '\0', 0);
		ArrayList<Character> chars = new ArrayList<Character>();
		chars.add(no_char);
		return chars;
	}
}
