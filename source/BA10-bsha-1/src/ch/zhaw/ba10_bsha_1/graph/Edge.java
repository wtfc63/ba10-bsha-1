package ch.zhaw.ba10_bsha_1.strategies;

public class Edge {


	protected Node source;
	protected Node destination;
	protected float probability;
	
	
	public Edge(Node src, Node dst, float probability) {
		this.source = src;
		this.destination = dst;
		this.probability = probability;
	}


	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}


	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}


	public float getProbability() {
		return probability;
	}

	public void setProbability(float probability) {
		this.probability = probability;
	}
}
