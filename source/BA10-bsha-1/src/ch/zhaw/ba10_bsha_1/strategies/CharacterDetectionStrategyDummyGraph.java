package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;

import android.util.Log;

import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.StrategyArgument;
import ch.zhaw.ba10_bsha_1.graph.GraphFactoryDummy;
import ch.zhaw.ba10_bsha_1.graph.GraphFactoryGraphMLImport;
import ch.zhaw.ba10_bsha_1.graph.IGraphFactory;
import ch.zhaw.ba10_bsha_1.graph.Node;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


public class CharacterDetectionStrategyDummyGraph extends BaseStrategy implements ICharacterDetectionStrategy {
	
	
	private Node root;
	
	
	public CharacterDetectionStrategyDummyGraph() {
		super();
		
		IGraphFactory factory = new GraphFactoryDummy();
		root = factory.createRoot();
	}
	
	
	@Override
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "DummyGraph";
	}

	@Override
	protected String getStrategyDescription() {
		return "Detect Character through a weighted graph, test dummy";
	}
	
	
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
		/*while ((character = root.consume(tmp)) != null) {
			character.setMicroGestures(micro_gestures);
			chars.add(character);
		}*/
		chars.addAll(root.consume(tmp, 1));
		for (Character c : chars) {
			if (c.toString().equals("none")) {
				chars.remove(c);
			}
		}
		return chars;
	}
}
