package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


public interface ICharacterDetectionStrategy extends IStrategy {
	public Collection<Character> detectCharacter(Collection<MicroGesture> micro_gestures);
}
