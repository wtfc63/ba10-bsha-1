package ch.zhaw.ba10_bsha_1.graph;


import java.io.File;


public class GraphFactoryGraphMLImport implements IGraphFactory {

	
	private String pathToFile;
	
	
	public GraphFactoryGraphMLImport(String path_to_file) {
		pathToFile = path_to_file;
	}
	
	
	@Override
	public Node createRoot() {
		Node root = null;
		if (pathToFile != null) {
			GraphMLParser parser = new GraphMLParser();
			File file = new File(pathToFile);
			root = parser.readFile(file);
		}
		
		return root;
	}

}