package ch.zhaw.ba10_bsha_1.fingerpad;


public class GraphFactoryGraphMLImport implements IGraphFactory {

	
	private String pathToFile;
	
	
	public GraphFactoryGraphMLImport(String path_to_file) {
		pathToFile = path_to_file;
	}
	
	
	@Override
	public Node createRoot() {
		Node root = null;
		if (pathToFile != null) {
			//TODO
		}
		return root;
	}

}
