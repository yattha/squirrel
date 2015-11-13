import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.PriorityQueue;


/*
 * Derek Moore & Heather Pedersen
 */

public class CodingTree {

	Map<Character, String> codes;
	List<Byte> bits;
	String bitString; 
	String textString;
	List<CharF> frequencies = new ArrayList<CharF>();
	PriorityQueue<Node> nodeQueue;
	Node finishedTree;
	



	public CodingTree(String message) {
		textString = message;
		codes = new HashMap<Character, String>();
		bitString = "";
		nodeQueue = new PriorityQueue<Node>();
		bits = new ArrayList<Byte>();		
		countCharFrequency();
		generateTree();
		generateCodes();
		encode();
	}
	
	public static String decode(String theBitString, Map<Character, String> theCodes) {
		StringBuilder result = new StringBuilder(), currentBits = new StringBuilder();		
		int len = theBitString.length(), curPos = 0;		
		
		while(len-- > 1) {			
			currentBits.append(theBitString.charAt(curPos++));
			if(theCodes.containsValue(currentBits.toString())) {
				for(Map.Entry<Character, String> e : theCodes.entrySet()){
					if(e.getValue().equals(currentBits.toString())){
						result.append(e.getKey().charValue());
						currentBits.delete(0, currentBits.length());						
					}
				}
			}					
		}		
		return result.toString();
	}

	private void encode() {
		bits.clear();
		int len = textString.length(), curPos = 0;		
		StringBuilder sb = new StringBuilder();
		while(len-- > 1) {			
			sb.append(codes.get(textString.charAt(curPos++)));						
		}
		bitString = sb.toString();		
		int index = 0; 
		while(index <  bitString.length() - 8) {
			if (bitString.length() - index > 7) {
				bits.add((byte) Integer.parseInt(bitString.substring(index, index + 8), 2));
				index += 8;
			} else {
				byte tempB = 0;
				for(int i =0; i<8; i++) {
					byte temp = (byte) Integer.parseInt(bitString.substring(index));
					tempB += temp << 7-i;
					index+=8;
				}
				bits.add(tempB);
			}			
		}		
	}

	
	private void countCharFrequency() {
		int len = textString.length(), curPos = 0;
		while(len-- > 1) {
			CharF tempCF = new CharF((int)textString.charAt(curPos++));
			int tempIndex = frequencies.indexOf(tempCF);

			if(tempIndex < 0) frequencies.add(tempCF);
			else frequencies.get(tempIndex).frequency++; 
		}
	}

	private void generateTree() {
		while(!frequencies.isEmpty()) {
			Node temp = new Node(frequencies.remove(0));
			nodeQueue.add(temp);
		}
		Node leftNode, rightNode, parentNode;

		while(nodeQueue.size() > 1) {
			parentNode = new Node(null);			
			leftNode = nodeQueue.poll();
			rightNode = nodeQueue.poll();
			parentNode.addleft(leftNode);
			parentNode.addRight(rightNode);
			nodeQueue.add(parentNode);
		}
		finishedTree = nodeQueue.poll();
	}


	private void generateCodes() {
		traverseTree(finishedTree, "");
	}


	private void traverseTree(Node n, String s) {				
		if(n.isLeaf()) {
			codes.put(new Character((char)n.data.ascii), s);
		}else {
		traverseTree(n.left, s += "0");		
		traverseTree(n.right, s = s.substring(0, s.length()-1) + "1");
		}
	}


	public String codeToString() {
		String result = "{";
		Iterator<Entry<Character, String>> it = codes.entrySet().iterator();
		Entry<Character, String> tempE;
		while(it.hasNext()) {
			tempE = it.next();
			result += tempE.getKey() + "=" + tempE.getValue() + ", ";
		}
		return result.substring(0, result.length() - 2) + "}";
	}


	private class Node implements Comparable<Node>{
		
		Node left;
		Node right;
		CharF data;
		int weight;

		Node(CharF d){
			data = d;
			left = null;
			right = null;
			if(d==null) weight = 0;
			else weight = d.frequency;
		}

		void addleft(Node n) {
			this.left = n;
			weight += n.weight;
		}

		void addRight(Node n) {
			this.right = n;
			weight += n.weight;
		}

		boolean isLeaf() {
			return Objects.isNull(left) && Objects.isNull(right);
		}

		public int compareTo(Node other) {
			return weight - other.weight;
		}

		public String toString() {
			return "" + weight;
		}
	}

	private class CharF {
		int ascii;
		int frequency;

		public CharF(int a){
			ascii =a;
			frequency = 1;
		}

		public boolean equals(Object other) {
			return ((CharF)other).ascii == ascii;
		}

		public String toString() {
			return (char)ascii + ", " + frequency;
		}
	}
}
