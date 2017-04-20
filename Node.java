/**
 * 
 */

/**
 * @author Jordan Milburn
 *
 */
import java.util.HashSet;
import java.util.ArrayList;

public class Node {//this class was designed with assignment two in mind as well. It only really needs id, label and city for assignment 1.
	int id; 
	double lat, lon;
	private ArrayList<Segment> outSegs;
	private ArrayList<Segment> inSegs;
	private HashSet<Road> adjacentRoads;
	Location location;
	public Node currentNode = this;
	public Node formerNode = null;
	//Fields for A Star algorithm
	public boolean visited = false;
	public double ectg = 0; //estimated cost to goal.
	public double csffs = 0; //Cost So Far From Start.
	//Fields for Articulation algorithm.
	public int depth = Integer.MAX_VALUE;
	public double count = Double.POSITIVE_INFINITY;
	public boolean articulationPoint = false;
	private HashSet<Node> neighbours;
	
	public Node(int id, double lat, double lon){
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.location = Location.newFromLatLon(lat, lon);
		inSegs = new ArrayList<Segment>();
		outSegs = new ArrayList<Segment>();
		adjacentRoads = new HashSet<Road>();
		neighbours = new HashSet<Node>();
	}
	public int getNodeId(){
		return this.id;
	}
	public double getNodeLat(){
		return this.lat;
	}
	public double getNodeLon(){
		return this.lon;
	}
	public ArrayList<Segment> getOutSegs(){
		return this.outSegs;
	}	
	public ArrayList<Segment> getInSegs(){
		return this.inSegs;
	}
	public HashSet<Road> getAdjacentRoads(){
		return this.adjacentRoads;
	}
	public Location getLocation() {
		return this.location;
	}
	public HashSet<Node> getNeighbours(){
		return this.neighbours;
	}
	public void reset(){
		visited = false;
		ectg = 0;
		csffs = 0;
		formerNode = null;
		depth = Integer.MAX_VALUE;
		articulationPoint = false;
		count = Double.POSITIVE_INFINITY;
		}
}
