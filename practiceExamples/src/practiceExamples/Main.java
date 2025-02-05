package practiceExamples;

public class Main {
    public static void main(String[] args) {
        LinkedList list = new LinkedList();

        // Append data to the linked list
        list.append(10);
        list.append(20);
        list.append(30);
        list.append(40);

        // Print the linked list
        list.printList();  // Output: 10 -> 20 -> 30 -> 40 -> null

        // Delete a node with a specific value
        list.delete(20);
        list.printList();  // Output: 10 -> 30 -> 40 -> null

        // Try to delete a non-existent value
        list.delete(100);  // Output: Value not found
    }
}
