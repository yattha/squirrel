import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//Some of the code sourced from http://courses.cs.washington.edu/courses/cse373/11wi/homework/5/BinaryHeap.java

/*
 * Derek Moore & Heather Pedersen
 */

public class MyPriorityQueue<T extends Comparable<T>>{

	List<T> members;

	MyPriorityQueue() {
		members = new ArrayList<T>();
	}

	public T poll() {		
		T result = peek();
		if (members.size() > 1) {
			members.remove(0);
			members.add(0, members.get(members.size() - 1));
			members.remove(members.size() - 1);
			bubbleDown();
		} else {
			members.remove(0);
		}
		return result;
	}

	public T peek() {		
		return members.get(0);
	}

	boolean add(T item) {
		members.add(item);		
		int i = members.size()-1;
		for (; i > 0 && item.compareTo(members.get((i -1) / 2)) < 0; i = ((i -1) / 2)) {
			members.set(i, members.get((i -1)/ 2));
			members.set((i-1)/2, item);
		}
		return true;
	}

	int size() {
		return members.size();
	}

	public String toString() {
		String result = "[";
		for(T item :members) {
			result += (item.toString() + ", ");
		}
		result = result.substring(0, result.length()-2) + "]";
		return result;
	}
	
	public void clear() {
		members.clear();
	}
	
	public boolean contains(T item) {
		return members.contains(item);
	}
	
	public Iterator<T> iterator() {
		return members.iterator();
	}
	
	public boolean offer(T item) {
		return add(item);
	}
	
	//Source : http://courses.cs.washington.edu/courses/cse373/11wi/homework/5/BinaryHeap.java
	private void bubbleDown() {
        int index = 0;        
        // bubble down
        while (hasLeftChild(index)) {
            // which of my children is smaller?
            int smallerChild = 2*index +1;            
            // bubble with the smaller child, if I have a smaller child
            if (hasRightChild(index)
                && members.get(2*index+1).compareTo(members.get(2*index+2)) > 0) {
                smallerChild =2*index+2;
            }             
            if (members.get(index).compareTo(members.get(smallerChild)) > 0) {
                swap(index, smallerChild);
            } else {
                // otherwise, get outta here!
                break;
            }            
            // make sure to update loop counter/index of where last el is put
            index = smallerChild;
        }        
    }

	private void swap(int index, int smallerChild) {
		T tempNode = members.get(index);
		members.set(index, members.get(smallerChild));
		members.set(smallerChild, tempNode);
		
	}

	private boolean hasLeftChild(int index) {
		
		return members.size() - 1 > 2*index;
	}
	
	private boolean hasRightChild(int index) {
		
		return members.size() - 1 > 2*index +1;
	}
    
}
