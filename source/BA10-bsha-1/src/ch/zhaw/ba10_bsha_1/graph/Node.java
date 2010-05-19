package ch.zhaw.ba10_bsha_1.graph;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;
import ch.zhaw.ba10_bsha_1.service.MicroGestureTester;


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

	public void addOutgoingEdge(Node node, float probability, int nr_of_samples) {
		Edge conn = new Edge(this, node, probability, nr_of_samples);
		node.addIncomingEdge(conn);
		outgoingEdges.add(conn);
	}

	public void addOutgoingEdge(Node node, float probability) {
		addOutgoingEdge(node, probability, 1);
	}
	
	public void addOutgoingEdge(Edge edge) {
		outgoingEdges.add(edge);
	}
	
	public void removeOutgoingEdge(Edge edge) {
		outgoingEdges.remove(edge);
	}
}