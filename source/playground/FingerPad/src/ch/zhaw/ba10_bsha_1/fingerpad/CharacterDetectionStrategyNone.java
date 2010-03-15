package ch.zhaw.ba10_bsha_1.fingerpad;


import java.util.ArrayList;
import java.util.Collection;


public class CharacterDetectionStrategyNone implements	ICharacterDetectionStrategy {

	@Override
	public Collection<Character> detectCharacter(Collection<MicroGesture> micro_gestures) {
		Character no_char = new Character(micro_gestures);
		ArrayList<Character> chars = new ArrayList<Character>();
		chars.add(no_char);
		return chars;
	}

	@Override
	public String toString() {
		return "None";
	}
}
