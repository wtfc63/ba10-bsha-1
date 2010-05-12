package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.StrategyArgument;
import ch.zhaw.ba10_bsha_1.graph.GraphFactoryGraphMLImport;
import ch.zhaw.ba10_bsha_1.graph.IGraphFactory;
import ch.zhaw.ba10_bsha_1.graph.Node;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


public class CharacterDetectionStrategyGraph extends BaseStrategy implements ICharacterDetectionStrategy {
	
	
	private Node root;
	
	
	public CharacterDetectionStrategyGraph() {
		super();
		
		IGraphFactory factory = new GraphFactoryGraphMLImport(arguments.get("Path").getArgumentValue());
		root = factory.createRoot();
	}
	
	
	@Override
	protected void initArguments() {
		StrategyArgument arg = new StrategyArgument(getStrategyName(), "Path", "/sdcard/test.xml", "Path of the GraphML file");
		arguments.put(arg.getArgumentName().toLowerCase(), arg);	
	}

	@Override
	protected String getStrategyName() {
		return "Graph";
	}

	@Override
	protected String getStrategyDescription() {
		return "Detect Character through a weighted graph, stored as a GraphML file";
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
