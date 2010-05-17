package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.graph.GraphFactoryDummy;
import ch.zhaw.ba10_bsha_1.graph.IGraphFactory;
import ch.zhaw.ba10_bsha_1.graph.Node;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Implementation of {@link ICharacterDetectionStrategy} using a 
 * VERY basic graph created by {@link GraphFactoryDummy}.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class CharacterDetectionStrategyDummyGraph extends BaseStrategy implements ICharacterDetectionStrategy {

	
	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private Node root;


	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	
	
	@Override
	public void initialize() {
		IGraphFactory factory = new GraphFactoryDummy();
		root = factory.createRoot();
	}

	@Override
	protected String getStrategyName() {
		return "DummyGraph";
	}

	@Override
	protected String getStrategyDescription() {
		return "Detect Character through a weighted graph, test dummy";
	}


	//---------------------------------------------------------------------------
	// Implementation of ICharacterDetectionStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Detect {@link Character}s out of the given {@link MicroGesture}s
	 * 
	 * @param micro_gestures
	 * @return
	 */
	@Override
	public Collection<Character> detectCharacter(Collection<MicroGesture> micro_gestures) {
		ArrayList<Character> chars = new ArrayList<Character>();
		Collection<MicroGesture> tmp = new ArrayList<MicroGesture>(micro_gestures.size());
		try {
			for (MicroGesture mg : micro_gestures) {
				tmp.add((MicroGesture) mg.clone());
			}
		} catch (CloneNotSupportedException ex) {
			tmp = micro_gestures;
		}
		chars.addAll(root.consume(tmp, 1));
		for (Character c : chars) {
			if (c.toString().equals("none")) {
				chars.remove(c);
			}
		}
		return chars;
	}
}
