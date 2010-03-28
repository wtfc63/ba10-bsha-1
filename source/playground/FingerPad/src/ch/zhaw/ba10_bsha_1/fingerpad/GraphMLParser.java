package ch.zhaw.ba10_bsha_1.fingerpad;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.*;

import android.util.Log;

public class GraphMLParser implements ContentHandler{
	private static final String TAG = "GraphMLParser"; 
	
	private Map<String, Node> nodes;
	private Vector<Pair> tempEdges;
	
	private int nodeID = 0;
	
	public class Pair {
		public String first;
		public String second;
		public Pair(String a, String b) {
			first = a;
			second = b;
		}
	}
	
	public Node readFile(File file) {
		nodes = new TreeMap<String, Node>();
		tempEdges = new Vector<Pair>();
		try{
			SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            xr.setContentHandler(this);
            xr.parse(new InputSource(new FileReader(file)));
		}
		catch(Throwable t){
			t.printStackTrace();
		}
		
		return nodes.get("0");
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endDocument() throws SAXException {
		
		for (Pair p : tempEdges) {
			Node a = nodes.get(p.first);
			Node b = nodes.get(p.second);
			Edge temp = new Edge(a, b, 1);
			a.addOutgoingEdge(temp);
			b.addIncomingEdge(temp);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		Log.e(TAG, "Skipped: " + name);
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		// TODO Auto-generated method stub
		
		if (localName.equalsIgnoreCase("edge")) {
			String source = atts.getValue("source");
			String target = atts.getValue("target");
			Log.v(TAG, source + " -> " + target);
			
			if (source != null && target != null) {
				Pair temp = new Pair(source, target);
				tempEdges.add(temp);
			}
			else {
				Log.e(TAG, "Malformed XML: " + localName);
			}
		}
		else if (localName.equalsIgnoreCase("node")) {
			String node = atts.getValue("id");
			String test = atts.getValue("test");
			String character = atts.getValue("character");
			Log.v(TAG, "Node: " + node);
			Node temp;
			if (test == null && character == null) {
				temp = new Node(nodeID++);
			}
			else if (character == null) {
				temp = new Node(nodeID++, test);
			}
			else {
				temp = new Node(nodeID++, test, character.charAt(0));
			}
			nodes.put(node, temp);
		}
		else {
			Log.v(TAG, "Element: " + localName + " " + qName + " " + uri);
		}
		
		
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
}
