package ch.zhaw.ba10_bsha_1.fingerpad;

import java.io.File;
import java.io.IOException;

import android.os.Environment;


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
		
		File b = new File("/sdcard/out.xml");
		GraphMLExport exp = new GraphMLExport();

		try {
			exp.writeFile(root, b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return root;
	}

}
