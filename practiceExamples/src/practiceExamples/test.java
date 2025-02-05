package practiceExamples;

public class test {

	 Node head; // head of the list

	
	 public void append(int data) {
		 
		 Node newNode = new Node(data);
		 
		 
		 if(head==null) {
			 
			 
			 head=newNode;
			 return;
		 }
		 
		 Node currentNode = head;
		 
		 while(currentNode.next!=null) {
			 
			 currentNode=currentNode.next;
		 }
		 currentNode.next = newNode;
		 
		 
	 }
	  public void printList() {
	        Node current = head;
	        while (current != null) {
	            System.out.print(current.data + " -> ");
	            current = current.next;
	        }
	        System.out.println("null");
	    }

}
