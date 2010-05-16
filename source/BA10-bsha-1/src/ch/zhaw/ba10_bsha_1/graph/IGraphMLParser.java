package ch.zhaw.ba10_bsha_1.graph;


/**
 * Interface to parse a GraphML file.
 * 
 * Uses the Factory-method design pattern.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public interface IGraphMLParser {
	
	/**
	 * Parse a GraphML file, create graph and return its root Node
	 * 
	 * @return
	 */
	public Node parse(String file_name);
}
