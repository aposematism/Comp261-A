import java.util.Stack;
import java.util.ArrayDeque;
import java.util.HashSet;

/** Articulation Point Iterative as best i could make it. I essentially tried to copy it directly from the Articulation Points part 2 notes. **/
public class ArticulationIterative {
	Node startNode;
	double count;
	APWrapper root;
	HashSet<Node> artPoints;
	
	public ArticulationIterative(Node n, double c, APWrapper r, HashSet<Node> aP){
		this.startNode = n;
		this.count = c;
		this.root = r;
		this.artPoints = aP;
		
	}
	
	public HashSet<Node> find(){
		Stack<APWrapper> stack = new Stack<APWrapper>();
		APWrapper start = new APWrapper(startNode, 1 , root);
		stack.push(start);
		
		while(!stack.isEmpty()){//Loop while there is still another node which has not been visited.
			APWrapper wrapper = stack.peek();
			Node candy = wrapper.current;
			candy.visited =  true;
			if(wrapper.children == null){//First loop.
				wrapper.children = new ArrayDeque();
				candy.depth = wrapper.depth;
				wrapper.reachBack = wrapper.depth;
				for(Node n : candy.getNeighbours()){
					if(n != wrapper.ancestor.current){
						wrapper.children.add(n);
					}
				}
			}
			else if(!wrapper.children.isEmpty()){//Main processing of children of that node. If the node exists, it first makes a wrapper which is pushed onto a stack. Else it compares the wrapper reachback and the child depth.
				Node child = wrapper.children.poll();/** Used ArrayDeque because it is very fast as a stack.*/
				if (child.depth < Integer.MAX_VALUE) {
					wrapper.reachBack = Math.min(wrapper.reachBack, child.depth);
				}
				else {
					stack.push(new APWrapper(child, candy.depth+1, wrapper));
				}
			}
			else{//Final loop when all children have been processed. Determines if something is a node.
				if(candy != startNode){
					if(wrapper.reachBack >= wrapper.ancestor.depth){
						wrapper.ancestor.current.articulationPoint = true;
						this.artPoints.add(wrapper.ancestor.current);
					}
					wrapper.ancestor.reachBack = Math.min(wrapper.ancestor.reachBack, wrapper.reachBack);
				}
				stack.pop();
			}
			
		}
		return this.artPoints;
	}
}
