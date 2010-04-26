package ch.zhaw.ba10_bsha_1.fingerpad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class Node {
	
	
	protected int id;
	protected String label;
	protected MicroGestureTester tester;
	protected char character;
	protected Collection<Edge> incomingEdges;
	protected Collection<Edge> outgoingEdges;
	
	
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
	
	public Collection<Character> consume(Collection<MicroGesture> micro_gestures, float probability) {
		ArrayList<Character> result = new ArrayList<Character>();
		if ((micro_gestures != null) && (micro_gestures.size() > 0)) {
			if (tester != null) {
				MicroGesture mg = micro_gestures.iterator().next();
				if (tester.validate(mg)) {
					micro_gestures.remove(mg);
					result.add(new Character(null, character, probability));
					for (Edge edge : outgoingEdges) {
						result.addAll(edge.getDestination().consume(micro_gestures, (edge.getProbability() * probability)));
					}
				}
			} else {
				for (Edge edge : outgoingEdges) {
					result.addAll(edge.getDestination().consume(micro_gestures, (edge.getProbability() * probability)));
				}
			}
		}
		Collections.sort(result);
		return result;
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
		outgoingEdges.add(conn);
	}
	
	public void addOutgoingEdge(Edge edge) {
		outgoingEdges.add(edge);
	}
	
	public void removeOutgoingEdge(Edge edge) {
		outgoingEdges.remove(edge);
	}
	
}