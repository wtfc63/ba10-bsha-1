package ch.zhaw.ba10_bsha_1.graph;


import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import ch.zhaw.ba10_bsha_1.R;
import ch.zhaw.ba10_bsha_1.service.DetectionService;
import ch.zhaw.ba10_bsha_1.service.MicroGestureTester;


/**
 * Create graph from a GraphML file, using XML Pull parsing
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class GraphMLPullParser implements IGraphMLParser {

	
	//---------------------------------------------------------------------------
	// Inner Class
	//---------------------------------------------------------------------------
	
	
	/**
	 * Temporary container for {@link Edge}s
	 */
	private class GraphEdge {
		int src;
		int tgt;
		float weight;
	}

	
	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	

	private static final String TAG = "GraphMLPullParser"; 
	private static enum TagType {KEY, NODE, EDGE, NONE};
	private static enum KeyType {VERSION, WEIGHT, TEST, CHARACTER, NONE};
	
	private XmlPullParser parser;
	private TagType    currentTagType;
	private KeyType    currentKeyType;
	private String     currentTag;
	private String[][] currentAttributes;
	
	private String versionID;
	private String charID;
	private String testID;
	private String weightID;
	
	@SuppressWarnings("unused")	private float  defaultVersion;
	@SuppressWarnings("unused")	private char   defaultChar;
	@SuppressWarnings("unused")	private String defaultTest;
	private float  defaultWeight;
	
	@SuppressWarnings("unused")	private float graphVersion;
	private Node rootNode;
	private Node currentNode;
	private GraphEdge currentEdge;
	
	private Hashtable<Integer, Node> nodes;
	private ArrayList<GraphEdge> edges;

	
	//---------------------------------------------------------------------------
	// Implementation of the IGraphMLParser interface
	//---------------------------------------------------------------------------
	

	/**
	 * Parse GraphML file, create graph and return its root {@link Node}
	 * 
	 * @return
	 */
	@Override
	public Node parse(String file_name) {
		Node root = null;
		initParser(file_name);
		if (parser != null) {
			try {
				int event_type = parser.getEventType();
				currentKeyType = KeyType.NONE;
				//Iterate through tags
				while (event_type != XmlPullParser.END_DOCUMENT) {
					switch (event_type) {
						case XmlPullParser.START_TAG :
							handleTag();
							break;
						case XmlPullParser.TEXT :
							handleText();
							break;
						case XmlPullParser.END_TAG :
							endTag();
							break;
						default :
							break;
					}
					event_type = parser.next();
				}
				//Build graph with read Nodes and Edges
				root = buildGraph();
			} catch (XmlPullParserException pull_ex) {
				Log.e(TAG, pull_ex.getMessage(), pull_ex);
			} catch (IOException ex) {
				Log.e(TAG, ex.getMessage(), ex);
			}
		}
		return root;
	}

	
	//---------------------------------------------------------------------------
	// Helper methods
	//---------------------------------------------------------------------------
	
	
	/**
	 * Initialize Parser, either with the GraphML file specified with its file name 
	 * or with the one included in the applications package
	 * 
	 * @param file_name
	 */
	private void initParser(String file_name) {
		File file = (file_name != null) ? new File(file_name) : null;
		//Use GraphML file at given location, if any was given and if there is a file there
		if ((file != null) && file.exists()) {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(false);
				factory.setValidating(false);
				parser = factory.newPullParser();
				parser.setInput(new FileReader(file));
			//Fall back to included GraphML file if anything goes wrong
			} catch (Exception ex) {
				parser = DetectionService.getContext().getResources().getXml(R.xml.graph);
			}
		//Use GraphML file included in application package if otherwise
		} else {
			parser = DetectionService.getContext().getResources().getXml(R.xml.graph);
		}
		nodes = new Hashtable<Integer, Node>();
		edges = new ArrayList<GraphEdge>();
	}

	/**
	 * Read attributes of current Tag into a 2D-array of Strings
	 * 
	 * @return
	 */
	private String[][] getAttributes() {
		String[][] attributes = new String[parser.getAttributeCount()][2];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i][0] = parser.getAttributeName(i);
			attributes[i][1] = parser.getAttributeValue(i);
		}
		return attributes;
	}
	
	/**
	 * Handle current XML-tag
	 */
	private void handleTag() {
		//Read current tag's name and its attributes
		currentTag = parser.getName();
		currentAttributes = getAttributes();
		//Current tag == <key>
		if (currentTag.equalsIgnoreCase("key")) {
			currentTagType = TagType.KEY;
			String attr = valueOfAttribute("attr.name");
			if (attr.equalsIgnoreCase("version")) {
				currentKeyType = KeyType.VERSION;
				versionID = valueOfAttribute("id");
			} else if (attr.equalsIgnoreCase("weight")) {
				currentKeyType = KeyType.WEIGHT;
				weightID = valueOfAttribute("id");
			} else if (attr.equalsIgnoreCase("test")) {
				currentKeyType = KeyType.TEST;
				testID = valueOfAttribute("id");
			} else if (attr.equalsIgnoreCase("character")) {
				currentKeyType = KeyType.CHARACTER;
				charID = valueOfAttribute("id");
			}
		//Current tag == <default>
		} else if (currentTag.equalsIgnoreCase("default")) {
			currentTagType = TagType.KEY;
		//Current tag == <node>
		} else if (currentTag.equalsIgnoreCase("node")) {
			currentTagType = TagType.NODE;
			currentNode = new Node(Integer.parseInt(valueOfAttribute("id")));
		//Current tag == <edge>
		} else if (currentTag.equalsIgnoreCase("edge")) {
			currentTagType = TagType.EDGE;
			currentEdge = new GraphEdge();
			currentEdge.src = Integer.parseInt(valueOfAttribute("source"));
			currentEdge.tgt = Integer.parseInt(valueOfAttribute("target"));
			currentEdge.weight = defaultWeight;
		//Current tag == <data>
		} else if (currentTag.equalsIgnoreCase("data")) {
			String key = valueOfAttribute("key");
			if (key.equals(versionID)) {
				currentTagType = TagType.NONE;
				currentKeyType = KeyType.VERSION;
			} else if (key.equals(weightID)) {
				currentKeyType = KeyType.WEIGHT;
			} else if (key.equals(testID)) {
				currentKeyType = KeyType.TEST;
			} else if (key.equals(charID)) {
				currentKeyType = KeyType.CHARACTER;
			} else {
				currentKeyType = KeyType.NONE;
			}
		//Otherwise
		} else { 
			currentTagType = TagType.NONE;
		}
	}
	
	/**
	 * Get the value of the current tag's attribute with the given name 
	 * 
	 * @param name
	 * @return
	 */
	private String valueOfAttribute(String name) {
		String attribute_value = null;
		for (int i = 0; ((attribute_value == null) && (i < currentAttributes.length)); i++) {
			if (currentAttributes[i][0].equalsIgnoreCase(name.toLowerCase())) {
				attribute_value = currentAttributes[i][1];
			}
		}
		return attribute_value;
	}
	
	/**
	 * Handle Text of the current element
	 */
	private void handleText() {
		//Discard whitespace
		String text = parser.getText().trim();
		if (!text.equals("")) {
			switch (currentTagType) {
				//If we're looking at text in a <key>-element, it has to be from an embedded <default>-element
				case KEY :
					switch (currentKeyType) {
						case VERSION :
							defaultVersion = Float.parseFloat(text);
							break;
						case WEIGHT :
							defaultWeight = Float.parseFloat(text);
							break;
						case TEST :
							defaultTest = text;
							break;
						case CHARACTER :
							defaultChar = text.charAt(0);
							break;
						default :
							break;
					}
					break;
				//If we're looking at text in a <node>- or <edge>-element, it has to be from an embedded <data>-element
				case NODE :
					//Fall through...
				case EDGE :
					switch (currentKeyType) {
						case WEIGHT :
							currentEdge.weight = Float.parseFloat(text);
							break;
						case TEST :
							currentNode.setTester(new MicroGestureTester(text));
							break;
						case CHARACTER :
							currentNode.setCharacter(text.charAt(0));
							break;
						default :
							break;
					}
					break;
				//Otherwise it's most likely a <data>-element, embedded in the main <graph>-element
				default :
					switch (currentKeyType) {
						case VERSION :
							graphVersion = Float.parseFloat(text);
							break;
						default :
							break;
					}
					break;
			}
		}
	}
	
	/**
	 * Handle end-tags
	 */
	private void endTag() {
		switch (currentTagType)  {
			case KEY :
				if (currentTag.equalsIgnoreCase("default")) {
					currentTagType = TagType.KEY;
					currentTag = "key";
					currentAttributes = null;
				} else if (currentTag.equalsIgnoreCase("key")) {
					currentTagType = TagType.NONE;
					currentTag = null;
					currentAttributes = null;
				}
				break;
			case NODE :
				//If it's a <data>-end-tag, skip and prepare for <node>-end-tag
				if (currentTag.equalsIgnoreCase("data")) {
					currentTagType = TagType.NODE;
					currentTag = "node";
					currentAttributes = null;
				//Otherwise commit Node to Hashtable
				} else if (currentTag.equalsIgnoreCase("node")) {
					if (nodes.isEmpty()) {
						rootNode = currentNode;
					}
					nodes.put(currentNode.getId(), currentNode);
					currentNode = null;
					currentTagType = TagType.NONE;
					currentTag = null;
					currentAttributes = null;
				}
				break;
			case EDGE :
				//If it's a <data>-end-tag, skip and prepare for <edge>-end-tag
				if (currentTag.equalsIgnoreCase("data")) {
					currentTagType = TagType.EDGE;
					currentTag = "edge";
					currentAttributes = null;
				//Otherwise commit Edge to ArrayList
				} else if (currentTag.equalsIgnoreCase("edge")) {
					edges.add(currentEdge);
					currentEdge = null;
					currentTagType = TagType.NONE;
					currentTag = null;
					currentAttributes = null;
				}
				break;
			default :
				break;
		}
	}
	
	/**
	 * Build graph from parsed {@link Node}s and {@link Edge}s
	 * 
	 * @return
	 */
	private Node buildGraph() {
		for (GraphEdge gr_edge : edges) {
			Node src = nodes.get(gr_edge.src);
			src.addOutgoingEdge(nodes.get(gr_edge.tgt), gr_edge.weight);
		}
		return rootNode;
	}
}
