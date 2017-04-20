import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JTextArea;

import java.io.File;


public class MainProgram extends GUI{//Loads of fields. I didn't end up using the Maps of the Road, Node and Segment but I left them in as i felt they would be very useful in assignment 2.
	//Drawing fields
	private double scale;
	private JTextArea outputT;
	private Location origin;
	private Graphics g;
	//Maps and Arrays
	public HashMap<Integer, Road> roadsMap;
	public HashMap<Integer, Node> nodesMap;
	public HashMap<Integer, Segment> segmentsMap;
	public HashMap<ArrayList<Location>, String> polygonMap;
	//Drawing arrays
	public ArrayList<Node> nodeList = new ArrayList<Node>();
	public ArrayList<Road> roadList = new ArrayList<Road>();
	public ArrayList<Segment> segmentList = new ArrayList<Segment>();
	public ArrayList<Road> selectedRoad;
	public ArrayList<Node> selectedNodes;
	public ArrayList<Segment> selectedSegment;
	public ArrayList<String> typeList = new ArrayList<String>();
	public TrieMap tMap;
	//File Objects for the data
	public File roads;
	public File nodes;
	public File segments;
	public File polygons;
	
	public HashSet<Node> artPoints;
	
	//Mouse Tracking
	private Node selectedNode = null;
	private Node secondSelectedNode = null;
	
	public MainProgram() throws Exception {//Does all the necessary business.
		initialize();
		onLoad(nodes, roads, segments, polygons);
		redraw();
		
	}
	@Override
	public void onLoad(File nodes, File roads, File segments, File polygons){//This took a bit of experimenting, but I decided it was most important to separate the loader so i can debug properly.		
				try {// Never quite figured out how to use the load menu, I just arbitrarily set it to load directly from the folder.
					roadsMap = FileLoader.loadRoad(roadList, tMap, roads);
					nodesMap = FileLoader.loadNode(roadsMap, nodeList, nodes);
					segmentsMap = FileLoader.loadSegments(roadsMap, nodesMap, segmentList, segments);
					polygonMap = FileLoader.loadPolygons(origin, scale, typeList, polygons);
				} 
				
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	}

	public void redraw(Graphics g){//Hub for graphics action. If I do Challenge, will include Polygon stuff.
		this.g = g;
		drawPolygons(this.g);
		drawSegments(this.g,  origin);
		drawNodes(this.g, origin);
	}
	
	public void onSearch(){//Trie structure is used for searching here. I think it is quite elegant although the string creation is a bit crude.
		selectedRoad = new ArrayList<Road>();
		selectedSegment = new ArrayList<Segment>();
		String streets = "Found locations starting with: '";
		String entry = getSearchBox().getText();
		if(tMap.contains(entry)){
			ArrayList<Road> trieNodeList = tMap.getAll(entry);
			for(Road r : trieNodeList){
				streets = streets + r.getLabel();
				streets = streets + ", ";
				for(Segment s : r.getSegmentList()){
					selectedSegment.add(s);
					
				}
			}
			outputT.setText(streets);
		}
		else{
			outputT.setText("Unable to find location starting with '" + entry + "'");
		}
		
		redraw(g);
	}
	public void onClick(MouseEvent e){//This just finds the closest point quickly and highlights it, I could probably make it a list to display all clicked.
		Location click = Location.newFromPoint(new Point(e.getX(), e.getY()), origin, scale);
		double min = Double.POSITIVE_INFINITY;
		Node closest = null;
		for(Node n : nodeList){
			if(n != null){
				double distance = n.getLocation().distance(click);
				if(distance < min){
					min = distance;
					closest = nodesMap.get(n.getNodeId());
				}
			}
		}
		if(selectedNode != null && secondSelectedNode != null){
			selectedNode = secondSelectedNode;
			secondSelectedNode = closest;
			discoverRoute();
		}
		else if(secondSelectedNode == null && selectedNode != null){
			secondSelectedNode = closest;
			discoverRoute();
		}
		else if(selectedNode == null){
			selectedNode = closest;
			selectedNodes = new ArrayList<Node>();
			selectedNodes.add(closest);
			redraw(g);
		}
		printInformation();
	}

	
	public void onMove(Move m){//Button action.
		switch (m) {
			case EAST:
				this.origin = this.origin.moveBy(1, 0);
				drawNodes(g, origin);
				drawSegments(g, origin);
				break;
			case NORTH:
				this.origin = this.origin.moveBy(0, 1);
				drawNodes(g, origin);
				drawSegments(g, origin);
				break;
			case SOUTH:
				this.origin = this.origin.moveBy(0,-1);
				drawNodes(g, origin);
				drawSegments(g, origin);
				break;
			case WEST:
				this.origin = this.origin.moveBy(-1, 0);
				drawNodes(g, origin);
				drawSegments(g, origin);
				break;
			case ZOOM_OUT://This should have been more complicated, but I lacked the time to really implement it properly. I fear I do not properly understand the way points work in graphics.
				this.scale = scale*0.8;
				drawNodes(g, origin);
				drawSegments(g, origin);
			case ZOOM_IN:
				this.scale = scale*1.2;
				drawNodes(g, origin);
				drawSegments(g, origin);
		}
	}
	
	public void discoverRoute(){
		AstarSearch search = new AstarSearch(selectedNode, secondSelectedNode);
		selectedSegment = new ArrayList<Segment>();
		selectedNodes = new ArrayList<Node>();
		Node goal = search.discover();
		selectedNodes.add(goal);
		while(goal.formerNode != null){
				for(Segment s : segmentList){
					if(s.getNode1().getNodeId() == goal.getNodeId() && s.getNode2().getNodeId() == goal.formerNode.getNodeId() || s.getNode2().getNodeId() == goal.getNodeId() && s.getNode1().getNodeId() == goal.formerNode.getNodeId()){
						selectedSegment.add(s);
						selectedNodes.add(goal.formerNode);
					}
				}
				goal = goal.formerNode;
			}
		redraw(g);
		for(Node y : nodeList){
			y.reset();
		}
	}
	
	public void printInformation(){
		String text = new String();
		String o;
		if(selectedNode != null && secondSelectedNode == null){
			text = "Closest Node " + selectedNode.getNodeId() + " on ";
			for(Road r : selectedNode.getAdjacentRoads()){
				o = r.getLabel();
				text = text + o;
			}
			outputT.setText(text);
		}
		else if(selectedNode != null && secondSelectedNode != null){
			String nodeA = new String();
			for(Road r : selectedNode.getAdjacentRoads()){
				nodeA = nodeA + "/" + r.getLabel();
			}
			String nodeB = new String();
			for(Road r : secondSelectedNode.getAdjacentRoads()){
				nodeB = nodeB + "/" + r.getLabel();
			}
			outputT.setText("Finding route from " + nodeA + " and " + nodeB + "\n");
			double distance = 0;
			for(Segment s : selectedSegment){
				distance = distance + s.getLength();
				outputT.append(s.getRoad().getLabel() + " : " + s.getLength() + "\n");
			}
			text = "The distance between both selected intersections: " + distance + "\n";
			outputT.append(text);
		}
		
	}
	
	public void articulationPoints(){
		artPoints = new HashSet<Node>();
		ArrayList<Node> artSearchNodes = new ArrayList<Node>();
		for(Node n : nodeList){
			artSearchNodes.add(n);
		}
		Node start = artSearchNodes.get(0);
		start.count = 0;
		int subTree = 0;
		for(Node n : start.getNeighbours()){
			if(n.count == Double.POSITIVE_INFINITY){
				ArticulationIterative aR = new ArticulationIterative(n, 1, new APWrapper(start, 0, null), artPoints);
				artPoints.addAll(aR.find());
				subTree++;
			}
			if(subTree > 1){
				artPoints.add(start);
			}
		}
		for(Node n : artSearchNodes){
			if(!n.visited){
				ArticulationIterative aR = new ArticulationIterative(n, 1, new APWrapper(n, 0, null), artPoints);
				artPoints.addAll(aR.find());
				subTree++;
			}
		}
		outputT.setText("There are " + artPoints.size() + " articulation points in this map.");
	}
	
	public void clear(){
		ArrayList<Node> selectedNodes = new ArrayList<Node>();
		ArrayList<Segment> selectedSegments = new ArrayList<Segment>();
		selectedNode = null;
		secondSelectedNode = null;
		for(Node n : nodeList){
			n.reset();
		}
	}
	
	public void drawNodes(Graphics g, Location origin){//Straightforward enough. It draws nodes.
		Graphics2D g2d = (Graphics2D)g;
		for(Node n : nodeList){
			if(n != null){
				g2d.setColor(Color.blue);
				Point p = n.getLocation().asPoint(origin, scale);
				g2d.fillOval(p.x-2, p.y-2, 4, 4);
			}
			if(n.articulationPoint){
				g2d.setColor(Color.yellow);
				Point p = n.getLocation().asPoint(origin, scale);
				g2d.fillOval(p.x-2, p.y-2, 4, 4);
			}
			
		}
		if(selectedNodes != null){
			for(Node n : selectedNodes){
				if(n != null){
					Point p = n.getLocation().asPoint(origin, scale);
					g2d.setColor(Color.green);
					g2d.fillOval(p.x-2, p.y-2, 4, 4);
				}
			}
		}

	}
	public void drawSegments(Graphics g, Location origin){//Draws Segments, redraws selectedSegments. I should reimplement it so selected Segments are removed from Segment List. Should add segments back to Segment List when deselected.
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.black);
		for(Segment s : segmentList){
			if(s != null){
				Point p = s.getLocations().get(0).asPoint(origin, scale);
				for(int i = 1; i < s.getLocations().size(); i++){
					Point q = s.getLocations().get(i).asPoint(origin, scale);
					g2d.drawLine(p.x, p.y, q.x, q.y);
					p = q;
				}
			}
		}
		g2d.setColor(Color.orange);
		if(selectedSegment != null){
			for(Segment s : selectedSegment){
				if(s != null){
					Point p = s.getLocations().get(0).asPoint(origin, scale);
					for(int i = 1; i < s.getLocations().size(); i++){
						Point q = s.getLocations().get(i).asPoint(origin, scale);
						g2d.drawLine(p.x, p.y, q.x, q.y);
						p = q;
					}
				}
			}
		}
	}
	public void drawPolygons(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		for (Map.Entry<ArrayList<Location>, String> entry : polygonMap.entrySet()) {
				ArrayList<Location> locations = entry.getKey();
				String type = entry.getValue();
				Path2D perimeter = new Path2D.Double();
				Point startPoint = locations.get(0).asPoint(origin, scale);
				perimeter.moveTo(startPoint.getX(), startPoint.getY());
				for(int j = 0; j < locations.size(); j++){
					Point p = locations.get(j).asPoint(origin, scale);
					perimeter.lineTo(p.getX(), p.getY());
				}
				perimeter.closePath();
				if (type.equals("0x2") || type.equals("0x7") || type.equals("0x8") || type.equals("0x13") || type.equals("0x19")
						 || type.equals("0x1a") || type.equals("0xa")|| type.equals("0xb") || type.equals("0xe")) { //grey
					g2d.setColor(Color.GRAY);
					g2d.fill(perimeter);
				}
				else if ( type.equals("0x28") || type.equals("0x3c") || type.equals("0x3e") || type.equals("0x40") || type.equals("0x41") || type.equals("0x45") || type.equals("0x47") || type.equals("0x48")) {
					g2d.setColor(new Color(10, 105, 148)); //blue
					g2d.fill(perimeter);
				}
				else if (type.equals("0x1e") || type.equals("0x16") || type.equals("0x17") || type.equals("0x18") ) {
					g2d.setColor(new Color(13, 115, 19)); //green
					g2d.fill(perimeter);
				}
				else {
					g2d.setColor(Color.BLUE);
					g2d.fill(perimeter);
				}
		}
		
	}
	
	public void initialize(){//Sorts everything before passing to other classes.
		origin = new Location(0,0);
		scale = 100;
		tMap = new TrieMap();
		outputT = getTextOutputArea();
		outputT.setText("Welcome to Auckland Map System implemented by Jordan!\n");
		
		try{
			roads = new File("roadID-roadInfo.tab");
			nodes = new File("nodeID-lat-lon.tab");
			segments = new File("roadSeg-roadID-length-nodeID-nodeID-coords.tab");
			polygons = new File("polygon-shapes.mp");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		new MainProgram();
	}
}
