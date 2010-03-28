package ch.zhaw.ba10_bsha_1.fingerpad;

import java.io.*;
import java.util.ArrayList;

public class GraphMLExport {
	private static String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
		"<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"\n" +
		"\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
		"\txsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns\n" +
		"\t http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n" +
		"<graph id=\"G\" edgedefault=\"directed\">\n";
	
	private static String footer = "</graph>\n</graphml>";
	
	
	private ArrayList<Edge> edges;
	private ArrayList<Node> nodes;
	
	public void writeFile(Node graph, File file) throws IOException {
		edges = new ArrayList<Edge>();
		nodes = new ArrayList<Node>();
		
        PrintWriter outputStream = null;

        try {
            outputStream = new PrintWriter(new FileWriter(file));
            outputStream.write(header);
            
            writeNode(graph, outputStream);
            
            outputStream.write(footer);
        }
        finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
	}
	
	private void writeNode(Node n, PrintWriter stream) throws IOException {
		if (!nodes.contains(n)) {
			stream.write("<node id=\"" + n.getId() + "\"");
			if (n.getLabel() != null)  {
				stream.write(" test=\"" + n.getLabel() + "\"");
			}
			if (n.getCharacter() != '\0') {
				stream.write(" character=\"" + n.getCharacter() + "\"");
			}
			stream.write("/>\n");
			nodes.add(n);
			for (Edge e : n.outgoingEdges) {
				writeEdge(e, stream);
			}
		}
	}
	
	private void writeEdge(Edge e, PrintWriter stream) throws IOException {
		if (!edges.contains(e)) {
			stream.write("<edge source=\"" + e.source.getId() + "\" ");
			stream.write("target=\"" + e.destination.getId() + "\"/>\n");
			edges.add(e);
			writeNode(e.destination, stream);
		}
	}
}
