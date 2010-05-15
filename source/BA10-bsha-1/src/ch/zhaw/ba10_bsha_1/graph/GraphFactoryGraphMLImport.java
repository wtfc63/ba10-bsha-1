package ch.zhaw.ba10_bsha_1.graph;


public class GraphFactoryGraphMLImport implements IGraphFactory {

	
	private String  pathToFile;
	
	
	public GraphFactoryGraphMLImport(String path_to_file) {
		this.pathToFile = path_to_file;
	}
	
	
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
