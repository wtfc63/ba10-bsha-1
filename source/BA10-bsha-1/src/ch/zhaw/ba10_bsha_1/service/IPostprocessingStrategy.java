package ch.zhaw.ba10_bsha_1.service;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.Character;


public interface IPostprocessingStrategy extends IStrategy {
	public Collection<Character> process(Collection<Character> chars);
}
