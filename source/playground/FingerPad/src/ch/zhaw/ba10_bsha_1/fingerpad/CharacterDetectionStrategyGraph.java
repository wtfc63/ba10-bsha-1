package ch.zhaw.ba10_bsha_1.fingerpad;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import android.os.Environment;
import android.util.Log;

//import edu.uci.ics.jung.graph.*;
//import edu.uci.ics.jung.graph.util.*;
//import edu.uci.ics.jung.io.GraphMLWriter;


public class CharacterDetectionStrategyGraph implements	ICharacterDetectionStrategy {
	private static final String TAG = "GraphStrategy"; 
	
	@Override
	public Collection<Character> detectCharacter(Collection<MicroGesture> micro_gestures) {
		/*<MicroGesture> gestures = new ArrayList<MicroGesture>(micro_gestures);
		
		// GRAPH TEST
		Character n1 = new Character();
		Character n2 = new Character();
		Character n3 = new Character();
		n1.setDetectedCharacter('a');
		n2.setDetectedCharacter('b');
		n3.setDetectedCharacter('c');
      
		Graph<Character, MicroGesture> g2 = new DirectedSparseMultigraph<Character, MicroGesture>();
		
		g2.addVertex(n1);
		g2.addVertex(n2);
		g2.addVertex(n3);
		
		g2.addEdge(gestures.get(0), n1, n2);
		g2.addEdge(gestures.get(1), n1, n3);
		g2.addEdge(gestures.get(2), n2, n3);
		
		GraphMLWriter<Character, MicroGesture> file = new GraphMLWriter<Character, MicroGesture>();
		try {
			File root = Environment.getExternalStorageDirectory();
			File path = new File(root, "graphtest");
			if (!path.exists()) {
				path.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
			
			// Save graph to disk
			file.save(g2, writer);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.v(TAG, "The graph g2 = " + g2.toString());
        
		
		*/
		Character no_char = new Character();
		ArrayList<Character> chars = new ArrayList<Character>();
		chars.add(no_char);
		
		return chars;
	}

	@Override
	public String toString() {
		return "Graph";
	}
}
