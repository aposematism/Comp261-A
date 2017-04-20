/**
 * 
 */

/**
 * @author Jordan Milburn
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
public class Road { //this class was designed with assignment two in mind as well. It only really needs id, label and city for assignment 1.
	private int id, type, speed, roadClass, oneWay, notCar, notPed, notBike;
	private String label, city;
	public HashMap<Integer, Node> nodes;
	public ArrayList<Segment> tranches;
	
	public Road (int id, int type,String label, String city, int oneWay, int speed, int roadClass, int notCar, int notPed, int notBike) {
		tranches = new ArrayList<Segment>();
		this.id = id;
		this.type = type;
		this.label = label;
		this.city = city;
		this.oneWay = oneWay;
		this.speed = speed;
		this.roadClass = roadClass;
		this.notCar = notCar;
		this.notPed = notPed;
		this.notBike = notBike;
	}

	public int getType(){
		return type;
	}
	public int getRoadId(){
		return id;
	}
	public String getLabel(){
		return label;
	}
	public String getCity(){
		return city;
	}
	public int getOneWay(){
		return oneWay;
	}
	public int getSpeed(){
		return speed;
	}
	public int getRoadClass(){
		return roadClass;
	}
	public int getNotCar(){
		return notCar;
	}
	public int getNotPed(){
		return notPed;
	}
	public int getNotBike(){
		return notBike;
	}
	public ArrayList<Segment> getSegmentList(){
		return tranches;
	}
	public HashMap<Integer, Node> getNodeMap(){
		return nodes;
	}
}
