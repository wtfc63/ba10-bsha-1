package ch.zhaw.ba10_bsha_1.fingerpad;

public class GraphFactoryDummy implements IGraphFactory {

	@Override
	public Node createRoot() {
		Node root = new Node(0);

		Node l = new Node(1, "l:dlr", 'l');
		Node L = new Node(11, "s:udr", 'L');
		l.addOutgoingEdge(L, 1);
		
		Node i = new Node(2, "s:dlr", 'i');
		Node i0 = new Node(20, "s:ur");
		i0.addOutgoingEdge(i, 1);
		
		Node a1 = new Node(31, "w:udl");
		Node a2 = new Node(32, "w:udr");
		a1.addOutgoingEdge(a2, 1);
		Node a21 = new Node(321, "n:ulr", 'a');
		a2.addOutgoingEdge(a21, 1);
		Node a22 = new Node(322, "s:ur");
		a2.addOutgoingEdge(a22, 1);
		Node a221 = new Node(3221, "s:dlr", 'a');
		a22.addOutgoingEdge(a221, 1);
		
		root.addOutgoingEdge(l, 1);
		root.addOutgoingEdge(i0, 1);
		root.addOutgoingEdge(i, 1);
		root.addOutgoingEdge(a1, 1);
		
		return root;
	}

}
