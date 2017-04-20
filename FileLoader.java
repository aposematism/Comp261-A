
import java.util.HashMap;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
import java.util.StringTokenizer;

public class FileLoader {//this class is designed to parse all 3 files differently.
	public static HashMap<Integer, Road> loadRoad(ArrayList<Road> roadList, TrieMap tMap, File roads) throws Exception{
		HashMap<Integer, Road> roadsMap = new HashMap<>();
		String roadLine;//different line strings for each method to prevent confusion.
		try{	
			BufferedReader buffRoad = new BufferedReader(new FileReader(roads));
			String topline = buffRoad.readLine();
			
			while((roadLine = buffRoad.readLine()) != null){//This is probably not a good method for reading it
				String[] fields = roadLine.split("\t");
				int id = Integer.parseInt(fields[0]);
				int type = Integer.parseInt(fields[1]);
				String label = fields[2];
				String city = fields[3];
				int  oneWay = Integer.parseInt(fields[4]);
				int speed = Integer.parseInt(fields[5]);
				int roadClass = Integer.parseInt(fields[6]);
				int notCar = Integer.parseInt(fields[7]);
				int notPed = Integer.parseInt(fields[8]);
				int notBike = Integer.parseInt(fields[9]);
				
				Road r = new Road(id, type, label, city, oneWay, speed, roadClass, notCar, notPed, notBike);
				roadsMap.put(id, r);
				roadList.add(r);
				tMap.add(r.getLabel(), r);
			}
			buffRoad.close();
			
		}
		catch(Exception e){
			e.printStackTrace();//I feel i should have a different type of exception for testing further on, but this is just initial code.
		}
		return roadsMap;
		}
	
	public static HashMap<Integer, Node> loadNode(HashMap<Integer, Road> roadsMap, ArrayList<Node> nodeList, File nodes) throws Exception{
		HashMap<Integer, Node> nodesMap = new HashMap<>();
		BufferedReader buffNodes = new BufferedReader(new FileReader(nodes));
		String nodeLine = buffNodes.readLine();;
		try{
			while(nodeLine != null){
				String[] fields = nodeLine.split("\t");
				int id = Integer.parseInt(fields[0]);
				Double lat = Double.parseDouble(fields[1]);
				Double lon = Double.parseDouble(fields[2]);
				Node n = new Node(id, lat, lon);
				nodeList.add(n);
				nodesMap.put(id, n);
				nodeLine = buffNodes.readLine();
			}
		buffNodes.close();
					
		}
		catch(Exception e){
			throw new Exception();
		}
		return nodesMap;
	}
	
	public static HashMap<Integer, Segment> loadSegments(HashMap<Integer, Road> roadsMap, HashMap<Integer, Node> nodesMap, ArrayList<Segment> segmentList, File segments) throws Exception{
		//This should probably call one the HashMap of Nodes to verify node positions.
		HashMap<Integer, Segment> segmentsMap = new HashMap<>();
		BufferedReader buffSegments = new BufferedReader(new FileReader(segments));
		String topline = buffSegments.readLine();
		String segmentLine = buffSegments.readLine();
		try{
			while(segmentLine != null){
				ArrayList<Location> locations = new ArrayList<>();
				String[] fields = segmentLine.split("\t");
				int id = Integer.parseInt(fields[0]);
				double length = Double.parseDouble(fields[1]);
				Node nodeId1 = nodesMap.get(Integer.parseInt(fields[2]));
				Node nodeId2 = nodesMap.get(Integer.parseInt(fields[3]));
				Road r = roadsMap.get(id);
				for(int i = 4; i < fields.length; i += 2){
					Double x = Double.parseDouble(fields[i]);
					Double y = Double.parseDouble(fields[i+1]);
					Location p = Location.newFromLatLon(x, y);
					locations.add(p);
				}
				Segment s = new Segment(id, length, nodeId1, nodeId2, r, locations);
				segmentList.add(s);
				segmentsMap.put(id, s);
				r.getSegmentList().add(s);
				nodeId1.getOutSegs().add(s);
				nodeId2.getInSegs().add(s);
				nodeId1.getAdjacentRoads().add(r);
				nodeId1.getNeighbours().add(nodeId2);
				if(r.getOneWay() == 0){//If this segment is not a one way road.
					nodeId2.getOutSegs().add(s);
					nodeId1.getInSegs().add(s);
					nodeId2.getAdjacentRoads().add(r);
					nodeId2.getNeighbours().add(nodeId1);
				}
				segmentLine = buffSegments.readLine();
			}
			buffSegments.close();
		}
		catch(Exception e){
			throw new Exception();
		}
		return segmentsMap;
	}
	
	public static HashMap<ArrayList<Location>, String> loadPolygons(Location origin, Double scale, ArrayList<String> typeList, File polygons){
		HashMap<ArrayList<Location>, String> polyMap = new HashMap<ArrayList<Location>, String>();
		try{
			BufferedReader buffPolygons = new BufferedReader(new FileReader(polygons));
			String line = buffPolygons.readLine();
			String type = null;
			while(line != null){
				if(line.startsWith("Type=")){
					type = line.substring(5);
					typeList.add(type);
				}
				if (line.startsWith("Data0=")) {
					ArrayList<Location> locations = new ArrayList<Location>();
	    			String data = line.substring(7);
	    			data = data.replace("(",  " ");
	    			data = data.replace(")", " ");
	    			data = data.replace(",", " ");
	    			StringTokenizer s = new StringTokenizer(data);
	    			
	    			while(s.hasMoreTokens()){
	    				Double c1 = Double.parseDouble(s.nextToken());
	    				Double c2 = Double.parseDouble(s.nextToken());
	    				Location l = Location.newFromLatLon(c1, c2);
	    				locations.add(l);
	    			}
	    		polyMap.put(locations, type);
				}
				line = buffPolygons.readLine();
			}
			buffPolygons.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return polyMap;
	}
}