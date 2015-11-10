import java.util.ArrayList;
import java.util.List;


public class MyPriorityQueue<T extends Comparable<T>>{
	
	List<T> members;
	
	MyPriorityQueue() {
		members = new ArrayList<T>();
			
	}
	
	T poll() {
		T temp= null;
		
		if(members.size()>0){
			temp = members.remove(0);
			members.add(0, members.get(members.size()));
			for(int i =  0; i < i;) {
				T left = members.get(2*i + 1);
				T right = members.get(2*i + 2);
				if(left.compareTo(right) < 0) {
					T tempNode = members.get(2*i +1);
					members.set(2*i+1, members.get(i));
					members.set(i, tempNode);
				} else {
					T tempNode = members.get(2*i +2);
					members.set(2*i+2, members.get(i));
					members.set(i, tempNode);
				}
			}
			
		}
		return temp;
	}
	
	void add(T item) {
		
		members.add(item);
		int i = members.size();
		
		
			for (; i > 1 && item.compareTo(members.get(i / 2)) < 0; i = i / 2) {
				members.set(i, members.get(i / 2));
			}
			
		
		
		
	}
	
	int size() {
		return members.size();
	}
}
