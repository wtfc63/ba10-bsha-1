package ch.zhaw.ba10_bsha_1.service;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.Character;


public class PostprocessingStrategyNone implements IPostprocessingStrategy {

	@Override
	public Collection<Character> process(Collection<Character> chars) {
		return chars;
	}

	@Override
	public String toString() {
		return "None";
	}
}
