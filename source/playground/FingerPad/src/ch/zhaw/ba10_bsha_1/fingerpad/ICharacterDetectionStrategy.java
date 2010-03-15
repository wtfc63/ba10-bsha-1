package ch.zhaw.ba10_bsha_1.fingerpad;

import java.util.Collection;

public interface ICharacterDetectionStrategy {
	public Collection<Character> detectCharacter(Collection<MicroGesture> micro_gestures);
	public String toString();
}
