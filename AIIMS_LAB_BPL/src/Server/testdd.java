package Server;

import java.util.HashMap;
import java.util.Map;

class testdd {

	 public static void main(String[] args) {
	        int[] arr = {1, 2, 2, 3, 4, 4, 4, 5};

	        Map<Integer, Integer> freqMap = new HashMap<>();

	        for (int num : arr) {
	            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
	        }

	        // Print frequency
	        for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
	            System.out.println("Number " + entry.getKey() + " appears " + entry.getValue() + " times.");
	        }
	    }
}