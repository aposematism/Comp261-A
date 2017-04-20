import java.util.PriorityQueue;

public class AstarSearch {
	private PriorityQueue<NodeTuple> fringe = new PriorityQueue<NodeTuple>();
	Node start;
	Node goal;
	
	public AstarSearch(Node s, Node g){
		this.start = s;
		this.goal = g;
	}
	
	public Node discover(){//Initializes the first item then goes forward.
		start.ectg = start.getLocation().distance(goal.getLocation());
		start.visited = true;
		NodeTuple startingNode = new NodeTuple(start, goal, 0, start.ectg);
		fringe.offer(startingNode);
		while(!fringe.isEmpty()){
			NodeTuple selected = fringe.poll();
			if(!selected.startNode.visited){//Updating the underlying node.
				selected.startNode.visited = true;
				selected.startNode.formerNode = selected.previousNode;
				selected.startNode.csffs = selected.currentCost;
			}
			if(selected.startNode == goal){//If you have found your path, break.
				break;
			}
			for(Segment s : selected.startNode.getOutSegs()){//For every segment out of startnode, create a NodeTupe if it has not been visited before.
				Node neighbour = null;
				if(selected.startNode == s.getNode1()){
					neighbour = s.getNode2();
				}
				if(selected.startNode == s.getNode2()){
					neighbour = s.getNode1();
				}
				if(!neighbour.visited){
					double ctn = selected.currentCost + s.getLength(); //Cost to Neighbour
					double efntg = ctn + neighbour.getLocation().distance(goal.getLocation());
					fringe.offer(new NodeTuple(neighbour, selected.startNode, ctn, efntg));
				}
			}
		}
		
		return goal;
	}
	
}
