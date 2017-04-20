import java.util.ArrayList;

public class TrieMap {
private TrieNode root;
	
	public TrieMap() {
		root = new TrieNode("#");
	}
	
	public void add(String word, Road r){//called when a road is created through the FileLoader, this just creates the nodes inside the TrieMap.
		TrieNode node = root;
		ArrayList<Character> traversed = new ArrayList<Character>();
		for(int j = 0; j < word.length(); j++){
			Character c = word.charAt(j);
			if(node.getChildren().get(c) == null){
				node.getChildren().put(c, new TrieNode(Character.toString(c)));
				node.setEnd(false);
				traversed.add(c);
				node = node.getChildren().get(c);
			}
			else{
				node = node.getChildren().get(c);
				traversed.add(c);
			}
		}
		node.setEnd(true);
		node.setCharacterList(traversed);
		node.setRoad(r);
	}
	public boolean contains(String word){//Checks for a word within the TrieMap. Turned out .startsWith(entry) method was inferior as it didn't work for boolean logic. Also prevents nullPointerExceptions, which are the bane of my existence.
		TrieNode node = root;
		for(int i = 0; i < word.length(); i++){
			Character c = word.charAt(i);
			if(node.getEnd()){
				return true;
			}
			else if(node.getChildren().get(c) == null){
				return false;
			}
			else{
				node = node.getChildren().get(c);
			}
			
		}
		return true;
	}
		
	
	public ArrayList<Road> getAll(String word){//This Method is called when Search is called in MainProgram. It goes through the tree to the node through the characters which make up the String 'word'. It then calls the recursive getAllFromNode and returns an ArrayList of Roads.
		TrieNode node = root;
		for(int i = 0; i < word.length(); i++){
			Character c = word.charAt(i);
			node = node.getChildren().get(c);
		}
		ArrayList<Road> roadList = new ArrayList<Road>();
		getAllFromNode(node, roadList);
		return roadList;
	}
	
	public ArrayList<Road> getAllFromNode(TrieNode n, ArrayList<Road> nL){//Recursive method for adding to an ArrayList of Roads. It goes through all the TrieNodes below the TrieNode n and returns their road objects.
		if(n.getRoad() != null){
			nL.add(n.getRoad());
		}
		for(Character c : n.getChildren().keySet()){
			getAllFromNode(n.getChildren().get(c), nL);
		}
		return nL;
	}
	
}