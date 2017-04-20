import java.util.ArrayList;
import java.util.HashMap;

public class TrieNode {
	
	private ArrayList<Character> characterList;//Characters so far.
	private Road r;//road attached to some nodes.
	private HashMap<Character, TrieNode> children;
	private boolean end;//is it an end node.
	
	public TrieNode(String marble){//So this was designed to act like a binary search tree in a sense.
		characterList = new ArrayList<Character>();
		for(int p = 0; p < marble.length(); p++){
			Character c = marble.charAt(p);
			characterList.add(c);
		}
		children = new HashMap<Character, TrieNode>();
		r = null;
	}
	
	public HashMap<Character, TrieNode> getChildren(){
		return children;
	}
	public ArrayList<Character> getCharacterList(){
		return characterList;
	}
	public Road getRoad(){
		return r;
	}
	public boolean getEnd(){
		return end;
	}
	public void setRoad(Road road){
		this.r = road;
	}
	public void setCharacterList(ArrayList<Character> characterList){
		this.characterList = characterList;
	}
	public void setEnd(boolean b){
		this.end = b;
	}
	

}
