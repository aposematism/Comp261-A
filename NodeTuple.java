public class NodeTuple implements Comparable<NodeTuple>{
	public Node startNode = null;
	public Node previousNode = null;
	public double currentCost = 0;
	public double totalCost = 0;
	
	public NodeTuple(Node start, Node from, double currentCost, double totalCost) {//Node Wrapper class.
		this.startNode = start;
		this.previousNode = from;
		this.currentCost = currentCost;
		this.totalCost = totalCost;
	}

	public int compareTo(NodeTuple nT) {//This is the magic right there. It compares the expected total cost of each item in the priority queue to determine which goes first.
		if (this.totalCost < nT.totalCost) { return -1; }
		else if (this.totalCost > nT.totalCost) { return 1; }
		else { return 0; }
	}
}
