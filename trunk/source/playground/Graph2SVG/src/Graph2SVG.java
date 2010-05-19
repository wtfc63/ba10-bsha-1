import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;

public class Graph2SVG {

	public static ArrayList<Node> nodes = new ArrayList<Node>();
	public static ArrayList<Edge> edges = new ArrayList<Edge>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frm = new JFrame();
		JPanel pnl = new JPanel() {
			public void paint(Graphics g) {
				super.paint(g);
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.BLACK);
				int radius = 20;
				for (Edge edge : edges) {
					g.drawLine(edge.startX, edge.startY + radius, edge.endX, edge.endY - radius);
				}
				for (Node node : nodes) {
					g.setColor(Color.LIGHT_GRAY);
					//g.drawRect(node.x - radius, node.y - radius, 2*radius, 2*radius);
					g.fillOval(node.x - radius, node.y - radius, 2*radius, 2*radius);
					g.setColor(Color.RED);
					g.drawString(Integer.valueOf(node.id).toString(), node.x - 5, node.y);
				}
			}
		};
		frm.setSize(1200, 720);
		pnl.setSize(1200, 720);
		frm.add(pnl);
		
		if (args.length < 1) {
			System.err.println("No arguments given, abourting...");
			System.exit(0);
		}
		
		String path = args[0];
		Node root = (new GraphMLPullParser()).parse(path);
		root.findLevels(1, 1);
		int v_depth = 0;
		for (Node node : nodes) {
			if (node.vertLevel > v_depth) {
				v_depth = node.vertLevel;
			}
		}
		int[] h_depths = new int[v_depth];
		int max_h_depth = 0;
		for (int i = 0; i < h_depths.length; i++) {
			h_depths[i] = 0;
			for (Node node : nodes) {
				if ((node.vertLevel == (i + 1)) && (node.horzLevel > h_depths[i])) {
					h_depths[i] = node.horzLevel;
				}
			}
			if (h_depths[i] > max_h_depth) {
				max_h_depth = h_depths[i];
			}
		}

		int width = (args.length > 3) ? Integer.parseInt(args[2]): (pnl.getWidth() - 80);
		int height = (args.length > 3) ? Integer.parseInt(args[3]): (pnl.getHeight() - 100);
		
		int v_gap = (height / (v_depth - 1));
		int h_gap = (width / (max_h_depth - 1));
		for (int i = 0; i < v_depth; i++) {
			for (int j = 0; j < h_depths[i]; j++) {
				for (Node node : nodes) {
					if ((node.vertLevel == (i + 1)) && (node.horzLevel == (j + 1))) {
						int h_offset = 30 + ((width - ((h_depths[i] + 1) * h_gap)) / 2);
						int pos_x = h_offset + ((j + 1) * h_gap);
						int pos_y = 30 + (i * v_gap);
						node.x = pos_x;
						node.y = pos_y;
						for (Edge in_edge : node.incomingEdges) {
							in_edge.endX = pos_x;
							in_edge.endY = pos_y;
						}
						for (Edge out_edge : node.outgoingEdges) {
							out_edge.startX = pos_x;
							out_edge.startY = pos_y;
						}
					}
				}
			}
		}
		pnl.repaint();

		if (args.length > 1) {
			String dest = args[1];
			try {
				generateSVG(dest);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			frm.setVisible(true);
		}
	}

	private static void generateSVG(String path) throws IOException {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:gml=\"http://graphml.graphdrawing.org/xmlns\" background-color=\"white\">\n");
		writer.write("  <defs>\n");
		writer.write("    <marker id=\"arrow\" refX=\"5\" refY=\"5\" markerUnits=\"userSpaceOnUse\" markerWidth=\"10\" markerHeight=\"10\" orient=\"auto\">\n");
		writer.write("      <path class=\"edge\" d=\"M0 0 10 5 0 10z\"/>\n");
		writer.write("    </marker>\n");
		writer.write("  </defs>\n");
		int radius = 30;
		for (Edge edge : edges) {
			int weight_x = edge.startX + ((edge.endX - edge.startX) / 3);
			int weight_y = edge.startY + ((edge.endY - edge.startY) / 3);
			writer.write("  <g>\n");
			writer.write("    <line class=\"edge\" x1=\"" + edge.startX + "\" y1=\"" + (edge.startY + radius) + "\" x2=\"" + edge.endX + "\" y2=\"" + (edge.endY - radius - 2) + "\" style=\"marker-end:url(#arrow);stroke:black;stroke-width:2\"/>\n");
			writer.write("    <text text-anchor=\"middle\" x=\"" + weight_x + "\" y=\"" + (weight_y + 2) + "\" style=\"font-size:18;fill:grey\">" + edge.probability + "</text>\n");
			writer.write("  </g>\n");
		}
		for (Node node : nodes) {
			writer.write("  <g class=\"node\">\n");
			writer.write("    <circle cx=\"" + node.x + "\" cy=\"" + node.y + "\" r=\"" + radius + "\" stroke=\"rgb(7,55,99)\" stroke-width=\"2\" fill=\"rgb(207,226,255)\"/>\n");
			writer.write("    <text text-anchor=\"middle\" x=\"" + node.x + "\" y=\"" + (node.y - (radius / 3)) + "\" style=\"font-size:24;font-weight:bold\">" + node.id + "</text>\n");
			writer.write("    <text text-anchor=\"middle\" x=\"" + node.x + "\" y=\"" + (node.y + (radius / 5)) + "\" style=\"font-size:12\">" + node.label + "</text>\n");
			if (node.character != '\0') {
				writer.write("    <text text-anchor=\"middle\" x=\"" + node.x + "\" y=\"" + (node.y + (2 * radius / 3)) + "\" style=\"font-size:18;fill:red\">" + node.character + "</text>\n");
			}
			writer.write("  </g>\n");
		}
		writer.write("</svg>\n");
		writer.close();
	}
}
