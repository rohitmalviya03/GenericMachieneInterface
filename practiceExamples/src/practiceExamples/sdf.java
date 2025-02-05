package practiceExamples;

class LinkedList {
    Node head; // head of the list

    // Constructor to create an empty linked list
    public LinkedList() {
        head = null;
    }

    // Method to add a new node at the end of the list
    public void append(int data) {
        Node newNode = new Node(data);

        // If the list is empty, make the new node the head
        if (head == null) {
            head = newNode;
            return;
        }

        // Otherwise, find the last node and add the new node after it
        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
    }

    // Method to print all the nodes in the linked list
    public void printList() {
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }

    // Method to delete the first occurrence of a specific value
    public void delete(int value) {
        // If the list is empty
        if (head == null) {
            System.out.println("List is empty");
            return;
        }

        // If the head is the node to delete
        if (head.data == value) {
            head = head.next;
            return;
        }

        Node current = head;
        while (current.next != null && current.next.data != value) {
            current = current.next;
        }

        // If the value is not found
        if (current.next == null) {
            System.out.println("Value not found");
        } else {
            // Skip the node to delete
            current.next = current.next.next;
        }
    }
}
