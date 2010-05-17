package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.StrategyArgument;
import ch.zhaw.ba10_bsha_1.graph.GraphFactoryGraphMLImport;
import ch.zhaw.ba10_bsha_1.graph.IGraphFactory;
import ch.zhaw.ba10_bsha_1.graph.Node;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Implementation of {@link ICharacterDetectionStrategy} using a graph created by an {@link IGraphFactory}.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class CharacterDetectionStrategyGraph extends BaseStrategy implements ICharacterDetectionStrategy {
	
	
	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private Node root;


	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	
	
	@Override
	public void initialize() {
		if (!hasArgument("Path")) {
			addArgument(new StrategyArgument(getStrategyName(), "Path", "/sdcard/ba10_bsha_1.xml", "Path of the GraphML file"));
		}
		
		IGraphFactory factory = new GraphFactoryGraphMLImport(getArgument("Path").getArgumentValue());
		root = factory.createRoot();	
	}

	@Override
	protected String getStrategyName() {
		return "Graph";
	}

	@Override
	protected String getStrategyDescription() {
		return "Detect Character through a weighted graph, stored as a GraphML file";
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
		//Character character = null;
		Collection<MicroGesture> tmp = new ArrayList<MicroGesture>(micro_gestures.size());
		try {
			for (MicroGesture mg : micro_gestures) {
				tmp.add((MicroGesture) mg.clone());
			}
		} catch (CloneNotSupportedException ex) {
			tmp = micro_gestures;
		}
		chars.addAll(root.consume(tmp, 1));
		ArrayList<Character> chars2 = new ArrayList<Character>();
		for (Character c : chars) {
			if (c.toString().contains("none")) {
				//chars.remove(c);
			}
			else {
				chars2.add(c);
			}
		}
		return chars2;
	}
}
