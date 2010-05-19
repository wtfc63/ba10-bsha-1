


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * Represents a Node in our graph. May carry a condition to test {@link MicroGesture}s 
 * given to consume as well as a {@link Character} it can evaluate {@link MicroGesture}s to.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class Node {


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private static final float MIN_PROBABILITY = 0.05f;
	
	protected int id;
	protected String label;
	protected MicroGestureTester tester;
	protected char character;
	protected Collection<Edge> incomingEdges;
	protected Collection<Edge> outgoingEdges;
	
	public int vertLevel = 0;
	public int horzLevel = 0;
	public int x;
	public int y;
	

	//---------------------------------------------------------------------------
	// Constructors
	//---------------------------------------------------------------------------

	
	public Node(int id, String test, char character) {
		this.id = id;
		this.tester = (test != null) ? new MicroGestureTester(test) : null;
		this.character = character;
		this.label = test;
		incomingEdges = new ArrayList<Edge>();
		outgoingEdges = new ArrayList<Edge>();
	}
	
	public Node(int id) {
		this(id, null, '\0');
	}
	
	public Node(int id, String test) {
		this(id, test, '\0');
	}


	//---------------------------------------------------------------------------
	// Character-recognition method
	//---------------------------------------------------------------------------
	
	
	/**
	 * Feed {@link MicroGesture}s to Node and return recognized {@link Character}s (recursively)
	 * 
	 * @param micro_gestures
	 * @param probability
	 */
	public Collection<Character> consume(Collection<MicroGesture> micro_gestures, float probability) {
		ArrayList<Character> result = new ArrayList<Character>();
		//Proceed if there are MicroGestures left to consume
		if ((micro_gestures != null) && (micro_gestures.size() > 0) && (probability > MIN_PROBABILITY)) {
			//Test if there is a MicroGestureTester associated with the Node 
			if (tester != null) {
				MicroGesture mg = micro_gestures.iterator().next();
				if (tester.validate(mg)) {
					//Remove positively evaluated MicroGestures from those left to feed
					micro_gestures.remove(mg);
					//Add the Node's Character to the results 
					result.add(new Character(null, character, probability));
					//Feed those MicroGestures left to recognize to all children
					for (Edge edge : outgoingEdges) {
						result.addAll(edge.getDestination().consume(micro_gestures, (edge.getProbability() * probability)));
					}
				}
			//Feed to all children if no MicroGestureTester present
			} else {
				for (Edge edge : outgoingEdges) {
					result.addAll(edge.getDestination().consume(micro_gestures, (edge.getProbability() * probability)));
				}
			}
		}
		//Sort recognized Characters by probability
		Collections.sort(result);
		return result;
	}
	
	
	public void findLevels(int vert_lvl, int horz_lvl) {
		if ((vertLevel < 1) && (horzLevel < 1)) {
			if (!Graph2SVG.nodes.contains(this)) {
				Graph2SVG.nodes.add(this);
			}
			for (Node node : Graph2SVG.nodes) {
				if ((node != this) && (node.vertLevel == vert_lvl) && (node.horzLevel == horz_lvl)) {
					horz_lvl++;
				}
			}
			vertLevel = vert_lvl;
			horzLevel = horz_lvl;
			ArrayList<Edge> edges = new ArrayList<Edge>(outgoingEdges);
			for (int i = 0; i < edges.size(); i++) {
				if (!Graph2SVG.edges.contains(edges.get(i))) {
					Graph2SVG.edges.add(edges.get(i));
				}
				edges.get(i).destination.findLevels(vertLevel + 1, i + 1);
			}
		}
	}


	//---------------------------------------------------------------------------
	// Getter-/Setter-methods
	//---------------------------------------------------------------------------


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
	public MicroGestureTester getTester() {
		return tester;
	}

	public void setTester(MicroGestureTester tester) {
		this.tester = tester;
	}

	
	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	
	public Collection<Edge> getIncomingEdges() {
		return incomingEdges;
	}

	public void setIncomingEdges(Collection<Edge> incomingEdges) {
		this.incomingEdges = incomingEdges;
	}
	
	public void addIncomingEdge(Edge edge) {
		incomingEdges.add(edge);
	}
	
	public void removeIncomingEdge(Edge edge) {
		incomingEdges.remove(edge);
	}

	
	public Collection<Edge> getOutgoingEdges() {
		return outgoingEdges;
	}

	public void setOutgoingEdges(Collection<Edge> outgoingEdges) {
		this.outgoingEdges = outgoingEdges;
	}

	public void addOutgoingEdge(Node node, float probability) {
		Edge conn = new Edge(this, node, probability);
		node.addIncomingEdge(conn);
		outgoingEdges.add(conn);
	}
	
	public void addOutgoingEdge(Edge edge) {
		outgoingEdges.add(edge);
	}
	
	public void removeOutgoingEdge(Edge edge) {
		outgoingEdges.remove(edge);
	}
}