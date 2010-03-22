package ch.zhaw.ba10_bsha_1.fingerpad;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import android.os.Environment;
import android.util.Log;


public class CharacterDetectionStrategyGraph implements	ICharacterDetectionStrategy {
	private static final String TAG = "GraphStrategy"; 
	
	private final String storePath;
	private Node root;
	
	
	public CharacterDetectionStrategyGraph() {
		storePath = "/sdcard/test.xml";
		IGraphFactory factory = new GraphFactoryGraphMLImport(storePath);//new GraphFactoryGraphMLImport(path);
		root = factory.createRoot();
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
		return "Graph";
	}
}
