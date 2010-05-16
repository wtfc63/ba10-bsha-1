package ch.zhaw.ba10_bsha_1.graph;


 /**
  * Creates graph from GraphML file.
  * 
  * @author Dominik Giger, Julian Hanhart
  */
public class GraphFactoryGraphMLImport implements IGraphFactory {


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private String pathToFile;
	

	//---------------------------------------------------------------------------
	// Constructor
	//---------------------------------------------------------------------------

	
	public GraphFactoryGraphMLImport(String path_to_file) {
		this.pathToFile = path_to_file;
	}
	

	//---------------------------------------------------------------------------
	// Implementation of the IGraphFactory interface
	//---------------------------------------------------------------------------


	/**
	 * Create graph and return its root Node
	 * 
	 * @return
	 */
	@Override
	public Node createRoot() {
		Node root = null;
		if (pathToFile != null) {
			IGraphMLParser parser = new GraphMLPullParser();
			root = parser.parse(pathToFile);
		}
		return root;
	}
}
