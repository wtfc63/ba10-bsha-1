package ch.zhaw.ba10_bsha_1.graph;

import java.io.*;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.*;

import android.util.Log;

public class GraphMLParser implements ContentHandler{
	private static final String TAG = "GraphMLParser"; 
	
	private Map<String, Node> nodes;
	private Stack<Pair> tempEdges;
	
	private int nodeID = 0;
	
	private enum Status { NODE, EDGE, DATATEST, DATACHARACTER, DATAWEIGHT, NONE };
	private Status status = Status.NONE;
	
	private String nodeId, nodeTest, nodeCharacter, edgeWeight;
	
	public class Pair {
		public String first;
		public String second;
		public Pair(String a, String b) {
			first = a;
			second = b;
		}
	}
	
	public class WeightedPair extends Pair {
		public float weight;
		public WeightedPair(String a, String b, float weight) {
			super(a, b);
			this.weight = weight; 
		}
	}
	
	public Node readFile(File file) {
		nodes = new TreeMap<String, Node>();
		tempEdges = new Stack<Pair>();
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
		if (status == Status.DATACHARACTER) {
			nodeCharacter = new String(ch, start, length);
		}
		else if (status == Status.DATATEST) {
			nodeTest = new String(ch, start, length);
		}
		else if (status == Status.DATAWEIGHT) {
			edgeWeight = new String(ch, start, length);
		}
	}

	@Override
	public void endDocument() throws SAXException {
		//Set<Entry<String, Node>> e = nodes.entrySet();
		
		while (!tempEdges.empty()) {
			Pair p = tempEdges.pop();
			Node a = nodes.get(p.first);
			Node b = nodes.get(p.second);
			float probability = (p instanceof WeightedPair) ? ((WeightedPair) p).weight : (float) 0.9;
			Edge temp = new Edge(a, b, probability);
			a.addOutgoingEdge(temp);
			b.addIncomingEdge(temp);
			//Log.v(TAG, "Edge: " + p.first + "->" +p.second);			
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (status == Status.NODE && localName.equalsIgnoreCase("node")) {
			status = Status.NONE;
			Node temp;
			if (nodeTest == null && nodeCharacter == null) {
				temp = new Node(nodeID++);
			}
			else if (nodeCharacter == null) {
				temp = new Node(nodeID++, nodeTest);
			}
			else {
				temp = new Node(nodeID++, nodeTest, nodeCharacter.charAt(0));
				//Log.v(TAG, "End " + localName + " " + nodeTest + " " + nodeCharacter);
			}		
			nodes.put(nodeId, temp);
			nodeTest = null;
			nodeCharacter = null;
			nodeId = null;
		}
		else if (status == Status.EDGE && localName.equalsIgnoreCase("edge")) {
			status = Status.NONE;
			edgeWeight = null;
		}
		else if (status == Status.DATATEST && localName.equalsIgnoreCase("data")) {
			status = Status.NODE;
		}
		else if (status == Status.DATACHARACTER && localName.equalsIgnoreCase("data")) {
			status = Status.NODE;
		}
		else if (status == Status.DATAWEIGHT && localName.equalsIgnoreCase("data")) {
			Pair last = tempEdges.pop();
			last = new WeightedPair(last.first, last.second, Float.parseFloat(edgeWeight));
			tempEdges.push(last);
			status = Status.EDGE;
		}
		
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
		
		
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		//Log.e(TAG, "Skipped: " + name);
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		// TODO Auto-generated method stub
		
		if (status == Status.NONE) { 
			if (localName.equalsIgnoreCase("edge")) {
				String source = atts.getValue("source");
				String target = atts.getValue("target");
				//Log.v(TAG, source + " -> " + target);
				
				if (source != null && target != null) {
					Pair temp = new Pair(source, target);
					tempEdges.push(temp);
				}
				else {
					Log.e(TAG, "Malformed XML: " + localName);
				}
				status = Status.EDGE;
			}
			else if (localName.equalsIgnoreCase("node")) {
				String node = atts.getValue("id");
				status = Status.NODE;
				if (node != null) {
					nodeId = node;
				}
				else {
					Log.e(TAG, "Malformed XML: " + localName);
				}
			}
			
		}
		else if (status == Status.NODE) {
			if (localName.equalsIgnoreCase("data")) {
				String key = atts.getValue("key");
				if (key.equalsIgnoreCase("d0")) {
					status = Status.DATACHARACTER;
				}
				else if (key.equalsIgnoreCase("d1")) {
					status = Status.DATATEST;
				}
				else {
					//Log.e(TAG, "Malformed XML: " + localName);
				}
			}
		}
		else if (status == Status.EDGE) {
			if (localName.equalsIgnoreCase("data")) {
				String key = atts.getValue("key");
				if (key.equalsIgnoreCase("d2")) {
					status = Status.DATAWEIGHT;
				}
			}
			//Log.e(TAG, "Malformed XML: " + localName);
		}
		
		//Log.v(TAG, "Element: " + localName + " " + qName + " " + uri);

	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
}
