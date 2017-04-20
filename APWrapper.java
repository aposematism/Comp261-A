import java.util.ArrayDeque;

public class APWrapper {/**Node Wrapper class for Node data point class. **/
	
	int depth = 0;
	int reachBack = 0;
	public APWrapper ancestor;
	public Node current;
	public ArrayDeque<Node> children;//ArrayDeque because it is extremely fast on a stack.
	
	public APWrapper(Node n, int d, APWrapper a){
		this.current = n;
		this.depth = d;
		this.ancestor = a;
	}

}
