package ch.zhaw.ba10_bsha_1.service;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.Character;


public interface ICharacterDetectionStrategy extends IStrategy {
	public Collection<Character> detectCharacter(Collection<MicroGesture> micro_gestures);
}
