package ch.zhaw.ba10_bsha_1.fingerpad;

public class GraphFactoryDummy implements IGraphFactory {

	@Override
	public Node createRoot() {
		Node root = new Node(0);

		Node first = new Node(1, "l:dlr", 'l');
		root.addOutgoingEdge(first, 1);
		Node first_first = new Node(11, "s:udr", 'L');
		first.addOutgoingEdge(first_first, 1);

		Node second = new Node(1, "s:dlr", 'i');
		root.addOutgoingEdge(second, 1);
		
		return root;
	}

}
