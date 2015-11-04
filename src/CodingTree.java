import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.PriorityQueue;


public class CodingTree {

	Map<Character, String> codes;
	List<Byte> bits; 
	String textString;
	List<CharF> frequencies = new ArrayList<CharF>();
	PriorityQueue<Node> nodeQueue;
	Node finishedTree;



	public CodingTree(String message) {
		textString = message;
		codes = new HashMap<Character, String>();
		bits = new ArrayList<Byte>();
		nodeQueue = new PriorityQueue<Node>();
		countCharFrequency();
		generateTree();
		generateCodes();
	}



























	public String Decode(List<Byte> theBits, Map<Character, String> theCodes){


		return "";
	}

	private void countCharFrequency() {
		int len = textString.length(), curPos = 0;
		while(len-- > 1) {
			//			System.out.print(((int) textString.charAt(curPos)) + ": ");
			//			System.out.println(textString.charAt(curPos));
			CharF tempCF = new CharF((int)textString.charAt(curPos++));
			int tempIndex = frequencies.indexOf(tempCF);

			if(tempIndex < 0) frequencies.add(tempCF);
			else frequencies.get(tempIndex).frequency++; 



		}

		//		for(int i = 0; i < frequencies.size(); i++ )System.out.println("[ " + i + ", " +frequencies.get(i).ascii + ", " 
		//				+frequencies.get(i).frequency);

	}





	private void generateTree() {
		while(!frequencies.isEmpty()) {
			Node temp = new Node(frequencies.remove(0));

			//System.out.println(temp.toString());


			nodeQueue.add(temp);
		}




		Node leftNode, rightNode, parentNode;

		while(nodeQueue.size() > 1) {
			parentNode = new Node(null);			
			leftNode = nodeQueue.poll();
			//System.out.println(leftNode);
			rightNode = nodeQueue.poll();
			//System.out.println(rightNode);
			parentNode.addleft(leftNode);
			parentNode.addRight(rightNode);
			//System.out.println(parentNode.toString() + "\n");
			nodeQueue.add(parentNode);
		}
		finishedTree = nodeQueue.poll();
		
		//System.out.println("here");
		//System.out.println(Objects.nonNull(finishedTree.right.right.right.right));
		//System.out.println(finishedTree.right.left.left.data.ascii);
	}



	private void generateCodes() {
		
		traverseTree(finishedTree, "");

	}



























	private void traverseTree(Node n, String s) {
				
		if(n.isLeaf()) {
			System.out.println((char)n.data.ascii + " " + s);
			codes.put(new Character((char)n.data.ascii), s);
		}else {
		
		
		//if(Objects.isNull(s)) s = "";

		traverseTree(n.left, s += "0");		
		traverseTree(n.right, s = s.substring(0, s.length()-1) + "1");
		}
		
//		if(Objects.nonNull(n.left)){
//			s += "0";
//			traverseTree(n.left, s);
//		}		
//		if(Objects.nonNull(n.right)){
//			s += "1";
//			traverseTree(n.right, s);
//		}

	}


	public String codeToString() {
		String result = "";
		Iterator<Entry<Character, String>> it = codes.entrySet().iterator();
		Entry<Character, String> tempE = it.next();
		while(it.hasNext()) result += tempE.getKey() + "=" + tempE.getValue() + ", ";
		return result;
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
