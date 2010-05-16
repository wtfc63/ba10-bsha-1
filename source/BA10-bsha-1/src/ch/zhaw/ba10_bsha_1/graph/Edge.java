package ch.zhaw.ba10_bsha_1.graph;


/**
 * Represents a weighted Edge of our graph
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class Edge {


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	

	protected Node source;
	protected Node destination;
	protected float probability;
	protected int nrOfSamples;
	
	
	//---------------------------------------------------------------------------
	// Constructor
	//---------------------------------------------------------------------------

	
	public Edge(Node src, Node dst, float probability, int nr_of_samples) {
		this.source = src;
		this.destination = dst;
		this.probability = probability;
		this.nrOfSamples = nr_of_samples;
	}
	
	public Edge(Node src, Node dst, float probability) {
		this(src, dst, probability, 1);
	}


	//---------------------------------------------------------------------------
	// Getter-/Setter-methods
	//---------------------------------------------------------------------------

	
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
	
	
	public int getNumberOfSamples() {
		return nrOfSamples;
	}
	
	public void setNumberOfSamples(int nr_of_samples) {
		this.nrOfSamples = nr_of_samples;
	}
}
