package ch.zhaw.ba10_bsha_1.graph;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import ch.zhaw.ba10_bsha_1.R;
import ch.zhaw.ba10_bsha_1.service.DetectionService;
import ch.zhaw.ba10_bsha_1.service.MicroGestureTester;


public class GraphMLPullParser implements IGraphMLParser {
	

	private static final String TAG = "GraphMLPullParser"; 
	private static enum TagType {KEY, NODE, EDGE, NONE};
	private static enum KeyType {VERSION, WEIGHT, TEST, CHARACTER, NONE};
	
	private class GraphEdge {
		int src;
		int tgt;
		float weight;
	}
	
	
	private XmlPullParser parser;
	private TagType    currentTagType;
	private KeyType    currentKeyType;
	private String     currentTag;
	private String[][] currentAttributes;
	
	private String versionID;
	private String charID;
	private String testID;
	private String weightID;
	
	private float  defaultVersion;
	private char   defaultChar;
	private String defaultTest;
	private float  defaultWeight;
	
	private float graphVersion;
	private Node rootNode;
	private Node currentNode;
	private GraphEdge currentEdge;
	
	private Hashtable<Integer, Node> nodes;
	private ArrayList<GraphEdge> edges;
	

	@Override
	public Node parse(String file_name) {
		Node root = null;
		initParser(file_name);
		if (parser != null) {
			try {
				int event_type = parser.getEventType();
				currentKeyType = KeyType.NONE;
				while (event_type != XmlResourceParser.END_DOCUMENT) {
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
				root = buildGraph();
			} catch (XmlPullParserException pull_ex) {
				Log.e(TAG, pull_ex.getMessage(), pull_ex);
			} catch (IOException ex) {
				Log.e(TAG, ex.getMessage(), ex);
			}
		}
		return root;
	}
	
	private void initParser(String file_name) {
		File file = (file_name != null) ? new File(file_name) : null;
		if ((file != null) && file.exists()) {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(false);
				factory.setValidating(false);
				parser = factory.newPullParser();
				parser.setInput(new FileReader(file));
			} catch (Exception ex) {
				parser = Resources.getSystem().getXml(R.xml.graph);
			}
		} else {
			parser = DetectionService.getContext().getResources().getXml(R.xml.graph);
		}
		nodes = new Hashtable<Integer, Node>();
		edges = new ArrayList<GraphEdge>();
	}

	private String[][] getAttributes() {
		String[][] attributes = new String[parser.getAttributeCount()][2];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i][0] = parser.getAttributeName(i);
			attributes[i][1] = parser.getAttributeValue(i);
		}
		return attributes;
	}
	
	private void handleTag() {
		currentTag = parser.getName();
		currentAttributes = getAttributes();
		if (currentTag.equalsIgnoreCase("key")) {
			currentTagType = TagType.KEY;
			String attr = findAttribute("attr.name");
			if (attr.equalsIgnoreCase("version")) {
				currentKeyType = KeyType.VERSION;
				versionID = findAttribute("id");
			} else if (attr.equalsIgnoreCase("weight")) {
				currentKeyType = KeyType.WEIGHT;
				weightID = findAttribute("id");
			} else if (attr.equalsIgnoreCase("test")) {
				currentKeyType = KeyType.TEST;
				testID = findAttribute("id");
			} else if (attr.equalsIgnoreCase("character")) {
				currentKeyType = KeyType.CHARACTER;
				charID = findAttribute("id");
			}
		} else if (currentTag.equalsIgnoreCase("default")) {
			currentTagType = TagType.KEY;
		} else if (currentTag.equalsIgnoreCase("node")) {
			currentTagType = TagType.NODE;
			currentNode = new Node(Integer.parseInt(findAttribute("id")));
		} else if (currentTag.equalsIgnoreCase("edge")) {
			currentTagType = TagType.EDGE;
			currentEdge = new GraphEdge();
			currentEdge.src = Integer.parseInt(findAttribute("source"));
			currentEdge.tgt = Integer.parseInt(findAttribute("target"));
			currentEdge.weight = defaultWeight;
		} else if (currentTag.equalsIgnoreCase("data")) {
			String key = findAttribute("key");
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
		} else {
			currentTagType = TagType.NONE;
		}
	}
	
	private String findAttribute(String name) {
		String attribute_value = null;
		for (int i = 0; ((attribute_value == null) && (i < currentAttributes.length)); i++) {
			if (currentAttributes[i][0].equalsIgnoreCase(name.toLowerCase())) {
				attribute_value = currentAttributes[i][1];
			}
		}
		return attribute_value;
	}
	
	private void handleText() {
		String text = parser.getText().trim();
		if (!text.equals("")) {
			switch (currentTagType) {
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
				if (currentTag.equalsIgnoreCase("data")) {
					currentTagType = TagType.NODE;
					currentTag = "node";
					currentAttributes = null;
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
				if (currentTag.equalsIgnoreCase("data")) {
					currentTagType = TagType.EDGE;
					currentTag = "edge";
					currentAttributes = null;
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
	
	private Node buildGraph() {
		for (GraphEdge gr_edge : edges) {
			Node src = nodes.get(gr_edge.src);
			src.addOutgoingEdge(nodes.get(gr_edge.tgt), gr_edge.weight);
		}
		return rootNode;
	}
}
