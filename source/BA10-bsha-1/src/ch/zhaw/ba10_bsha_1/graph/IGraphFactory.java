package ch.zhaw.ba10_bsha_1.graph;


/**
 * Interface to create a graph.
 * 
 * Uses the Factory-method design pattern.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public interface IGraphFactory {
	
	/**
	 * Create graph and return its root Node
	 * 
	 * @return
	 */
	public Node createRoot();
}
