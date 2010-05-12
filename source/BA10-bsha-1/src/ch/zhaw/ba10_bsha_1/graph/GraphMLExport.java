package ch.zhaw.ba10_bsha_1.strategies;

import java.io.*;
import java.util.ArrayList;

import android.util.Log;

public class GraphMLExport {
	private static String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
		"<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"\n" +
		"\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
		"\txsi:schemaLcation=\"http://graphml.graphdrawing.org/xmlns\n" +
		"\t http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n" +
		"\t<key id=\"d0\" for=\"node\" attr.name=\"character\" attr.type=\"string\">\n" +
		"\t\t<default></default>\n" +
		"\t</key>\n" +
		"\t<key id=\"d1\" for=\"node\" attr.name=\"test\" attr.type=\"string\">\n" +
		"\t\t<default></default>\n" +
		"\t</key>\n" +
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
                Log.v("GraphStrategy", "Graph saved.");
            }
        }
	}
	
	private void writeNode(Node n, PrintWriter stream) throws IOException {
		if (!nodes.contains(n)) {
			stream.write("<node id=\"" + n.getId() + "\">\n");
			if (n.getLabel() != null)  {
				stream.write("\t<data key=\"d1\">" + n.getLabel() + "</data>\n");
			}
			if (n.getCharacter() != '\0') {
				stream.write("\t<data key=\"d0\">" + n.getCharacter() + "</data>\n");
			}
			stream.write("</node>\n");
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
