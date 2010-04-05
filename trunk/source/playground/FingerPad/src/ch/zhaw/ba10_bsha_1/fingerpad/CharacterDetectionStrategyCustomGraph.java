package ch.zhaw.ba10_bsha_1.fingerpad;


import java.util.ArrayList;
import java.util.Collection;


public class CharacterDetectionStrategyCustomGraph implements ICharacterDetectionStrategy {
	
	
	private final String storePath;
	private Node root;
	
	
	public CharacterDetectionStrategyCustomGraph(String path) {
		storePath = path;
		IGraphFactory factory = new GraphFactoryDummy();//new GraphFactoryGraphMLImport(path);
		root = factory.createRoot();
	}
	
	public Node getRoot() {
		return root;
	}
	

	@Override
	public Collection<Character> detectCharacter(Collection<MicroGesture> micro_gestures) {
		ArrayList<Character> chars = new ArrayList<Character>();
		Character character = null;
		Collection<MicroGesture> tmp = new ArrayList<MicroGesture>(micro_gestures.size());
		try {
			for (MicroGesture mg : micro_gestures) {
				tmp.add((MicroGesture) mg.clone());
			}
		} catch (CloneNotSupportedException ex) {
			tmp = micro_gestures;
		}
		while ((character = root.consume(tmp)) != null) {
			character.setMicroGestures(micro_gestures);
			chars.add(character);
		}
		return chars;
	}

	@Override
	public String toString() {
		return "Custom Graph";
	}
}
