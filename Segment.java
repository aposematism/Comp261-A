/**
 * 
 */

/**
 * @author Jordan Milburn
 *
 */
import java.util.List;

public class Segment {//this class was designed with assignment two in mind as well. It only really needs id, label and city for assignment 1.
	private int id;
	private Node nodeId1, nodeId2;
	private double length;
	private Road road;
	private List<Location> locations;
	public Segment (int id, double length, Node nodeId1, Node nodeId2, Road r, List<Location> locations){//I specifically made it nodes rather than Integers so i can call nodes from segments
		this.id = id;
		this.length = length;
		this.nodeId1 = nodeId1;
		this.nodeId2 = nodeId2;
		this.road = r; // Implement something for this later. I want to implement function for clicking one segment and getting its info.
		this.locations = locations;
	}
	public int getNodeId(){
		return this.id;
	}
	public Node getNode1(){
		return this.nodeId1;
	}
	public Node getNode2(){
		return this.nodeId2;
	}
	public double getLength(){
		return this.length;
	}
	
	public Road getRoad(){
		return this.road;
	}
	
	public List<Location> getLocations(){
		return this.locations;
	}
}
